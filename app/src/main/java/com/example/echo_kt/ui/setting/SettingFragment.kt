package com.example.echo_kt.ui.setting

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.echo_kt.R
import com.example.echo_kt.api.showToast
import com.example.echo_kt.databinding.SettingFragmentBinding
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.ui.main.MainViewModel
import com.example.echo_kt.utils.checkPermissions
import com.example.echo_kt.utils.getPermission
import com.example.echo_kt.utils.saveBackground
import com.example.echo_kt.utils.stringForTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray


@AndroidEntryPoint
class SettingFragment : Fragment() {

    private lateinit var settingViewModel: SettingViewModel
    private val mainViewModel: MainViewModel by activityViewModels()
    private var _binding: SettingFragmentBinding? = null
    private val binding get() = _binding!!
    private var updateJob: Job? = null
    private lateinit var startActivity: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.data != null && it.resultCode == Activity.RESULT_OK) {
                    showToast("获取图片成功")
                    lifecycleScope.launch(Dispatchers.IO) {
                        saveBackground(it.data!!.data!!)
                        val drawable: Drawable? =
                            Drawable.createFromPath(requireActivity().filesDir.path + "/echo_bg.jpg")
                        drawable?.run {
                            withContext(Dispatchers.Main) {
                                (this@SettingFragment.requireActivity()
                                    .findViewById<ViewGroup>(R.id.main_container)).background = drawable
                            }
                        }
                    }
                } else {
                    showToast("获取图片失败")
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingViewModel = if (PlayerManager.instance.getSettingViewModel() != null) {
            PlayerManager.instance.getSettingViewModel()!!
        } else {
            ViewModelProvider(this).get(SettingViewModel::class.java)
        }
        binding.lifecycleOwner = this
        binding.vmSetting = settingViewModel
        initClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initClick() {
        if (settingViewModel.countdownBean.value == null) {
            //设置定时关闭开关键，开启时打开定时任务，目前默认值为60秒
            binding.switchTimeOff.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
                if (b) {
                    settingViewModel.countdownBean.postValue(
                        SettingViewModel.CountdownBean(600000, true)
                    )
                    PlayerManager.instance.startTimer(settingViewModel)
                } else {
                    PlayerManager.instance.cleanCountdown()
                    settingViewModel.countdownBean.value = (
                            SettingViewModel.CountdownBean(0, false)
                            )
                }
            }
        }
        binding.apply {
            seekBarOff.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    binding.countdown.text = stringForTime(seekBar!!.progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    Log.i("TAG", "onStopTrackingTouch:" + seekBar!!.progress.toLong())
                    settingViewModel.countdownBean.value =
                        (SettingViewModel.CountdownBean(seekBar.progress.toLong(), true))
                }
            })
            btnUpdate.setOnClickListener {
                progressBar.isGone = false
                updateState.isGone = true
                updateJob?.cancel()
                updateJob = lifecycleScope.launch {
                    vmSetting!!.updateAudioUrl {
                        showToast("歌曲地址更新完成")
                        progressBar.isGone = true
                        updateState.isGone = false
                    }
                }
            }
            ivBack.setOnClickListener { it.findNavController().navigateUp() }
            mainViewModel.floatWindowViewState.observe(viewLifecycleOwner, {
                switchFloatWindow.isChecked = it
            })
            switchFloatWindow.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
                if (b) {
                    mainViewModel.showFloatWindow()
                } else {
                    mainViewModel.closeFloatWindow()
                }
            }
            btnChangeBackground.setOnClickListener {
                val permissions = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (!checkPermissions(permissions)) {
                    getPermission(requireActivity(), permissions, 2002)
                }
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_PICK
                    //直接打开系统相册  不设置会有选择相册一步（例：系统相册、QQ浏览器相册）
                    data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                startActivity.launch(intent)
            }
        }
    }
}