package com.example.echo_kt.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.echo_kt.R
import com.example.echo_kt.databinding.FragmentMusicBinding
import com.example.echo_kt.play.AudioObserver
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.utils.stringForTime

class MusicFragment : Fragment(), AudioObserver {

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.context?.let { PlayerManager.instance.register(this) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.pvm = viewModel
        binding.onClick = onClick()
        initSeekBar()
    }

    private fun initSeekBar() {
        binding.seekBar.apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.tvProgress.text = stringForTime(seekBar.progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if (viewModel.maxProgress.get()!=null&&viewModel.maxProgress.get()!!>0){
                        PlayerManager.instance.seekTo(seekBar!!.progress)
                    }
                }

            })
        }
    }

    override fun onDestroy() {
        this.context?.let { PlayerManager.instance.unregister(this) }
        _binding = null
        super.onDestroy()
    }

    private fun onClick(): View.OnClickListener {
        return View.OnClickListener {
            when (it.id) {
                R.id.imgListMode -> PlayerManager.instance.switchPlayMode()
                R.id.imgPrevious -> PlayerManager.instance.previous()
                R.id.imgPlayStart -> PlayerManager.instance.controlPlay()
                R.id.imgNext -> PlayerManager.instance.next()
                R.id.imgAudioList -> NavHostFragment.findNavController(this)
                    .navigate(R.id.action_playFragment_to_audioListDialogFragment)
            }
        }
    }

    override fun onPlayMode(playMode: Int) {
        when (playMode) {
            PlayList.PlayMode.ORDER_PLAY_MODE -> viewModel.playModePic.set(R.drawable.ic_order_play)
            PlayList.PlayMode.RANDOM_PLAY_MODE -> viewModel.playModePic.set(R.mipmap.random)
            PlayList.PlayMode.SINGLE_PLAY_MODE -> viewModel.playModePic.set(R.drawable.ic_cycle_play)
        }
    }

    override fun onReset() {
        viewModel.reset()
    }

    override fun onProgress(currentDuration: Int, totalDuration: Int) {
        viewModel.currentDuration.set(stringForTime(currentDuration))
        viewModel.maxDuration.set(stringForTime(totalDuration))
        viewModel.playProgress.postValue(currentDuration)
        viewModel.maxProgress.set(totalDuration)
    }
}