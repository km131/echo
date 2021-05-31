package com.example.echo_kt.ui.main.songListFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.adapter.SongListAdapter
import com.example.echo_kt.adapter.SongViewModel
import com.example.echo_kt.api.showToast
import com.example.echo_kt.data.AudioBean
import com.example.echo_kt.databinding.AddToPlaylistDialogBinding
import com.example.echo_kt.databinding.AudioListDialogItemBinding
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.room.AppDataBase
import com.example.echo_kt.ui.main.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddToPlayListDialog: BottomSheetDialogFragment() {
    private lateinit var binding: AddToPlaylistDialogBinding
    private val sViewModel: SongViewModel by activityViewModels()
    private val hViewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddToPlaylistDialogBinding.inflate(inflater, container, false)
        initOptionList()
        return binding.root
    }

    private fun initOptionList() {
        binding.rvItemList.adapter = SongListAdapter(hViewModel.songList.value!!).apply {
            setOnItemClickListener(object : SongListAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val songList = hViewModel.songList.value!![position].apply {
                        list!!.add(sViewModel.audioBean.get()!!)
                    }
                    GlobalScope.launch {
                        AppDataBase.getInstance().customSongListDao().updateSongList(songList)
                    }
                    showToast("添加成功")
                    findNavController().navigateUp()
                }

                override fun onItemLongClick(view: View, position: Int) {

                }
            })
        }
    }
}