package com.example.echo_kt.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.echo_kt.R
import com.example.echo_kt.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment(),AudioObserver {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = MainFragmentBinding.inflate(inflater, container, false)
        val viewPage = binding.vpHome
        val btnNav = binding.navView
        viewPage.adapter = MainPagerAdapter(this)
        btnNav.run {
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_home -> viewPage.setCurrentItem(0, false)
                    R.id.menu_video -> viewPage.setCurrentItem(1, false)
                    R.id.main -> viewPage.setCurrentItem(2, false)
                }
                // 这里注意返回true,否则点击失效
                true
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onPlayMode(playMode: Int) {
       when (playMode){
           PlayList.PlayMode.ORDER_PLAY_MODE -> viewModel.playModePic.set(R.mipmap.play_order)
           PlayList.PlayMode.RANDOM_PLAY_MODE ->viewModel.playModePic.set(R.mipmap.play_random)
           PlayList.PlayMode.SINGLE_PLAY_MODE ->viewModel.playModePic.set(R.mipmap.play_single)

       }
    }

    override fun onReset() {
        viewModel.reset()
    }

    override fun onAudioBean(audioBean: AudioBean) {
        viewModel.songName.set(audioBean.name)
        viewModel.singer.set(audioBean.singer)
        viewModel.maxDuration.set(stringForTime(audioBean.duration))
        viewModel.maxProgress.set(audioBean.duration)
        viewModel.albumPic.set(audioBean.albumId)
//        //在io线程中查询是否收藏
//        lifecycleScope.launch {
//            val bean = withContext(Dispatchers.IO) {
//                AppDataBase.getInstance()
//                    .//historyAudioDao()
//                    .findAudioById(audioBean.id)
//            }
//            viewModel.collect.set(bean != null)
//        }
    }

    private fun stringForTime(duration: Int): String? {
        TODO("Not yet implemented")
    }

}