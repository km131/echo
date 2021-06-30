package com.example.echo_kt.ui.setting

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.core.view.isGone
import com.example.echo_kt.api.showToast
import com.example.echo_kt.databinding.SettingFragmentBinding
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.ui.main.ListSongViewModel
import com.example.echo_kt.util.stringForTime
import com.example.echo_kt.util.updateUrl
import kotlinx.coroutines.*

class SettingFragment : Fragment() {

    companion object {
        fun newInstance() = SettingFragment()
    }

    private lateinit var viewModel: SettingViewModel
    private var _binding:SettingFragmentBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= SettingFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val a = ViewModelProvider(this).get(SettingViewModel::class.java)
        viewModel = if (PlayerManager.instance.getSettingViewModel()!=null){
            PlayerManager.instance.getSettingViewModel()!!
        }else{
            ViewModelProvider(this).get(SettingViewModel::class.java)
        }
        //initLiveData()
        binding.lifecycleOwner = this
        // TODO: Use the ViewModel
        binding.vmHome=viewModel
        onClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClick() {
        if (viewModel.countdownBean.value == null) {
            //设置定时关闭开关键，开启时打开定时任务，目前默认值为60秒
            binding.switchTimeOff.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
                if (b) {
                    viewModel.countdownBean.postValue(
                        SettingViewModel.CountdownBean(600000, true)
                    )
                    PlayerManager.instance.startTimer(viewModel)
                } else {
                    PlayerManager.instance.cleanCountdown()
                    viewModel.countdownBean.value = (
                            SettingViewModel.CountdownBean(0, false)
                            )
                }
            }
        }

        binding.seekBarOff.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.countdown.text = stringForTime(seekBar!!.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.i("TAG", "onStopTrackingTouch:" + seekBar!!.progress.toLong())
                viewModel.countdownBean.value =
                    (SettingViewModel.CountdownBean(seekBar.progress.toLong(), true))
            }
        }
        )

        binding.apply {
            btnUpdate.setOnClickListener {
                progressBar.isGone = false
                updateState.isGone = true
                GlobalScope.launch(Dispatchers.IO){
                    updateUrl()
                    Log.i("TAG", "onClick: 更新内存中地址")
                    PlayList.instance.initHistoryList()
                    ListSongViewModel().scanHistorySong()
                    withContext(Dispatchers.Main) {
                            showToast("歌曲地址更新完成")
                            progressBar.isGone = true
                            updateState.isGone = false
                    }
                }
            }
        }
    }
}