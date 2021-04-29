package com.example.echo_kt.ui.main.songListFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.echo_kt.adapter.MultipleChoiceListAdapter
import com.example.echo_kt.data.AudioBean
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.databinding.FragmentAddSongsBinding
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.room.AppDataBase
import com.example.echo_kt.ui.main.HomeViewModel
import com.example.echo_kt.util.readCustomPlayList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddSongsFragment : Fragment() {
    companion object {
        fun newInstance() = AddSongsFragment()
    }

    private var _binding: FragmentAddSongsBinding? = null
    private val args: AddSongsFragmentArgs by navArgs()
    private val viewModel: HomeViewModel by activityViewModels()
    private val binding get() = _binding!!
    private var audioList : MutableList<AudioBean> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentAddSongsBinding.inflate(inflater,container,false)
        initToolBar()
        initOnClick()
        initData()
        binding.rvSongs.adapter=MultipleChoiceListAdapter(audioList)
        return binding.root
    }

    private fun initData() {
        audioList = when (args.songListType){
            SongListType.LOCAL -> PlayList.instance.getLocalList().toMutableList()
            SongListType.HISTORY -> PlayList.instance.getHistoryList().toMutableList()
            SongListType.LIKE -> mutableListOf()
        }
    }

    private fun initOnClick() {
        binding.apply {
            setAddSongs {
                GlobalScope.launch {
                    val songList:MutableList<AudioBean> = mutableListOf()
                    val list = (rvSongs.adapter as MultipleChoiceListAdapter).mSelectedPositions
                    list.forEach { i: Int, b: Boolean ->
                        if (b){
                            songList.add(audioList[i])
                        }
                    }
                    val songListBean:SongListBean = viewModel.songList.value!![viewModel.songlistIndex]
                    if (songListBean.list==null){
                        songListBean.list=songList
                    }else{
                        songListBean.list!!.addAll(songList)
                    }
                    AppDataBase.getInstance().customSongListDao().updateSongList(songListBean)
                }
            }
        }
    }

    private fun initToolBar() {

    }
}