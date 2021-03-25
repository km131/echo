package com.example.echo_kt.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echo_kt.R
import com.example.echo_kt.adapter.MyErectAdapter
import com.example.echo_kt.adapter.SongListAdapter
import com.example.echo_kt.data.ErectBean
import com.example.echo_kt.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private var _binding:HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter= MyErectAdapter(initErectAdapter())
    private lateinit var adapterSongList:SongListAdapter

    private fun initErectAdapter(): MutableList<ErectBean> {
        return mutableListOf(
            ErectBean(R.mipmap.localmusic,"本地音乐"),
            ErectBean(R.mipmap.lovemusic,"我喜欢的"),
            ErectBean(R.mipmap.historymusic,"最近播放")
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.vmHome=viewModel
        binding.rvHome.adapter=adapter
        adapterSongList=SongListAdapter(viewModel.songListBean)
        binding.rvMySongList.adapter=adapterSongList
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    private fun onClick(){
        binding.ibtSetting.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_settingsFragment)
        }
    }

}