package com.example.echo_kt.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.echo_kt.R
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.databinding.MainFragmentBinding
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import java.util.Formatter

class MainFragment : Fragment(),AudioObserver {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val viewPage = binding.vpHome
        val btnNav = binding.navView
        viewPage.adapter = MainPagerAdapter(this)
        //禁止滑动
        viewPage.isUserInputEnabled = false
        btnNav.run {
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_home -> viewPage.setCurrentItem(0, false)
                    R.id.menu_video -> viewPage.setCurrentItem(1, false)
                    R.id.menu_my -> viewPage.setCurrentItem(2, false)
                }
                // 这里注意返回true,否则点击失效
                true
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        binding.mainVM=viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.context?.let { PlayerManager.instance.register(this) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //将bind置空，控制binding生命周期
        _binding=null
    }

    private fun onClick() {
        binding.playView.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_playFragment)
        }
        binding.btnMusicList.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_audioListDialogFragment)
        }
        binding.btnPlay.setOnClickListener{
            PlayerManager.instance.controlPlay()
        }
    }
    override fun onPlayMode(playMode: Int) {
       when (playMode){
           PlayList.PlayMode.ORDER_PLAY_MODE ->{
               viewModel.playModePic.set(R.mipmap.order)
               viewModel.playModeText.set("顺序播放")
           }
           PlayList.PlayMode.RANDOM_PLAY_MODE ->{
               viewModel.playModePic.set(R.mipmap.random)
               viewModel.playModeText.set("随机播放")
           }
           PlayList.PlayMode.SINGLE_PLAY_MODE ->{
               viewModel.playModePic.set(R.mipmap.single)
               viewModel.playModeText.set("单曲循环")
           }
       }
    }

    override fun onReset() {
        viewModel.reset()
    }

    override fun onAudioBean(audioBean: SongBean) {
        viewModel.songName.set(audioBean.songName)
        viewModel.singer.set(audioBean.author)
        viewModel.maxProgress.set(100000)
        viewModel.albumPic.set(audioBean.albumUrl)
    }

    override fun onPlayStatus(playStatus: Int) {
        super.onPlayStatus(playStatus)
        viewModel.playStatus.set(playStatus)
    }

    private fun stringForTime(duration: Int): String? {
        val totalSeconds = duration/1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds/60)%60

        return Formatter().format("%02d:%02d",minutes,seconds).toString();
    }
}