package com.example.echo_kt.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.echo_kt.api.showToast
import com.example.echo_kt.databinding.SettingFragmentBinding
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.ui.main.MainViewModel
import com.example.echo_kt.utils.stringForTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private lateinit var settingViewModel: SettingViewModel
    private val mainViewModel: MainViewModel by activityViewModels()
    private var _binding: SettingFragmentBinding? = null
    private val binding get() = _binding!!
    private var updateJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        }
    }
}