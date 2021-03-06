package com.example.echo_kt.ui.main.songListFragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.echo_kt.R
import com.example.echo_kt.adapter.SongListItemAdapter
import com.example.echo_kt.adapter.SongViewModel
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.databinding.FragmentHistorySongBinding
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.room.AppDataBase
import com.example.echo_kt.ui.main.ListSongViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistorySongFragment : Fragment() {

    private val viewModel: ListSongViewModel by activityViewModels()
    private var _binding: FragmentHistorySongBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistorySongBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showDeleteDialog(position: Int, songList: MutableList<SongBean>?) {
        val normalDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        normalDialog.setTitle("确定删除该歌曲？")
        normalDialog.setMessage(songList!![position].songName)
        normalDialog.setPositiveButton(
            "确定"
        ) { _, _ ->
            lifecycleScope.launch(Dispatchers.IO) {
                AppDataBase.getInstance().historyAudioDao()
                    .deleteAudio(songList[position].id)
                withContext(Dispatchers.Main) {
                    val s = PlayList.instance.setHistoryList(songList[position])
                    if (s) {
                        viewModel.listSongData.value!!.removeAt(position)
                        binding.rvLocalSong.adapter!!.notifyItemRemoved(position)
                        Toast.makeText(binding.root.context, "删除成功", Toast.LENGTH_SHORT).show()
                    } else Toast.makeText(binding.root.context, "删除失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // 显示
        normalDialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.isNull = false
        viewModel.scanHistorySong()
        viewModel.listSongData.observe(this.viewLifecycleOwner, {
            if (viewModel.listSongData.value != null) {
                binding.rvLocalSong.adapter =
                    SongListItemAdapter(viewModel.listSongData.value ?: mutableListOf()).apply {
                        setOnItemClickListener(object : SongListItemAdapter.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                val vm: SongViewModel by activityViewModels()
                                vm.audioBean.set(viewModel.listSongData.value!![position])
                                view.findNavController()
                                    .navigate(R.id.action_historySongFragment_to_bottomDialogFragment)
                            }

                            //长按从历史列表删除
                            override fun onItemLongClick(view: View, position: Int) {
                                showDeleteDialog(position, viewModel.listSongData.value!!)
                            }
                        })
                    }
            } else {
                binding.isNull = true
            }
        })
        onClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onClick() {
        //播放全部
        binding.setOnClickPlayAll {
            viewModel.listSongData.value?.let { it1 ->
                PlayList.instance.switchAudioList(it1)
            }
            PlayerManager.instance.startAll()
        }
    }
}