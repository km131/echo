package com.example.echo_kt.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.echo_kt.R
import com.example.echo_kt.databinding.PlayFragmentBinding
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import java.util.*

class PlayFragment : Fragment(),AudioObserver {

    companion object {
        fun newInstance() = PlayFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var _binding:PlayFragmentBinding?=null
    private val binding get()=_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.context?.let { PlayerManager.instance.register(this) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= PlayFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSeekBar()
    }

    private fun initSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.tvProgress.text=stringForTime(seekBar.progress)
                Log.i("", "onProgressChanged: 进度改变")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                PlayerManager.instance.seekTo(seekBar!!.progress)
            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        binding.pvm=viewModel
        binding.onClick=onClick()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(context as AppCompatActivity).get(MainViewModel::class.java)
    }

    private fun onClick(): View.OnClickListener? {
        return View.OnClickListener{
            when(it.id){
                R.id.imgListMode -> PlayerManager.instance.switchPlayMode()
                R.id.imgPrevious -> PlayerManager.instance.previous()
                R.id.imgPlayStart -> PlayerManager.instance.controlPlay()
                R.id.imgNext -> PlayerManager.instance.next()
                R.id.imgAudioList -> NavHostFragment.findNavController(this).navigate(R.id.action_playFragment_to_audioListDialogFragment)
            }
        }
    }

    override fun onPlayMode(playMode: Int) {
        when (playMode){
            PlayList.PlayMode.ORDER_PLAY_MODE -> viewModel.playModePic.set(R.mipmap.order)
            PlayList.PlayMode.RANDOM_PLAY_MODE ->viewModel.playModePic.set(R.mipmap.random)
            PlayList.PlayMode.SINGLE_PLAY_MODE ->viewModel.playModePic.set(R.mipmap.single)
        }
    }

    override fun onReset() {
        viewModel.reset()
    }

    override fun onProgress(currentDuration: Int, totalDuration: Int) {
        viewModel.currentDuration.set(stringForTime(currentDuration))
        viewModel.playProgress.set(currentDuration)
    }

    private fun stringForTime(duration: Int): String? {
        val totalSeconds = duration/1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds/60)%60

        return Formatter().format("%02d:%02d",minutes,seconds).toString();
    }
}