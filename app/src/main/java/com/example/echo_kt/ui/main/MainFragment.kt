package com.example.echo_kt.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.R
import com.example.echo_kt.databinding.MainFragmentBinding
import com.example.echo_kt.play.PlayList

class MainFragment : Fragment(),AudioObserver {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

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
//        禁止滑动
//        viewPage.isUserInputEnabled = false
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
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
        binding.mainVM=viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //将bind置空，控制binding生命周期
        _binding=null
    }

    private fun onClick() {
        binding.playView.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_playFragment)
            val lise:MutableList<AudioBean> = PlayList.instance.readLocalPlayList(BaseApplication.getContext())
            Log.i("hhh", "onClick: " +lise)
        }
        binding.btnMusicList.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_audioListDialogFragment)
        }
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

    override fun onPlayStatus(playStatus: Int) {
        super.onPlayStatus(playStatus)
    }

    private fun stringForTime(duration: Int): String? {
        TODO("Not yet implemented")
    }
}