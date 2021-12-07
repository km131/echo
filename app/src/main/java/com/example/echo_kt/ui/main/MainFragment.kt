package com.example.echo_kt.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.echo_kt.R
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.databinding.MainFragmentBinding
import com.example.echo_kt.play.AudioObserver
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager

class MainFragment : Fragment(), AudioObserver {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.vpHome.adapter = MainPagerAdapter(this)
        //禁止滑动
        binding.vpHome.isUserInputEnabled = false
        binding.navView.run {
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_home -> binding.vpHome.setCurrentItem(0, false)
//                    R.id.menu_video -> binding.vpHome.setCurrentItem(1, false)
                    R.id.menu_info -> binding.vpHome.setCurrentItem(2, false)
                }
                // 这里注意返回true,否则点击失效
                true
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainVM=viewModel
        onClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.context?.let { PlayerManager.instance.register(this) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
        viewModel.albumPic.set(audioBean.albumUrl)
    }

    override fun onPlayStatus(playStatus: Int) {
        super.onPlayStatus(playStatus)
        viewModel.playStatus.set(playStatus)
    }
}