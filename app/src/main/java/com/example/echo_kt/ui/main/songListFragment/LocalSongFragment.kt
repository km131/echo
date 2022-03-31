package com.example.echo_kt.ui.main.songListFragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.echo_kt.R
import com.example.echo_kt.adapter.SongListItemAdapter
import com.example.echo_kt.adapter.SongViewModel
import com.example.echo_kt.api.showToast
import com.example.echo_kt.databinding.FragmentLocalSongBinding
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.ui.main.ListSongViewModel
import com.example.echo_kt.utils.checkPermissions
import com.example.echo_kt.utils.getPermission

class LocalSongFragment : Fragment() {

    private val viewModel: ListSongViewModel by activityViewModels()
    private var _binding: FragmentLocalSongBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocalSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.scanLocalSong()
        binding.vm = viewModel
        viewModel.listSongData.observe(this.viewLifecycleOwner, {
            if (viewModel.listSongData.value != null) {
                binding.rvLocalSong.adapter =
                    SongListItemAdapter(viewModel.listSongData.value ?: mutableListOf()).apply {
                        setOnItemClickListener(object : SongListItemAdapter.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                val vm: SongViewModel by activityViewModels()
                                vm.audioBean.set(viewModel.listSongData.value!![position])
                                view.findNavController()
                                    .navigate(R.id.action_localSongFragment_to_bottomDialogFragment)
                            }
                        })
                    }
            }
        })
        onClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClick() {
        binding.scan.setOnClickListener {
            val permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET
            )
            //下载
            if (!checkPermissions(permissions)) {
                getPermission(requireActivity(), permissions, 2002)
            }
            viewModel.scanLocalSong()
            if (viewModel.listSongData.value != null && viewModel.listSongData.value!!.size > 0) {
                binding.vm = viewModel
                binding.rvLocalSong.adapter = SongListItemAdapter(viewModel.listSongData.value!!)
            }
        }
        binding.playAll.setOnClickListener {
            viewModel.listSongData.value?.let {
                if (it.size>0){
                    PlayList.instance.switchAudioList(it)
                    PlayerManager.instance.startAll()
                }else showToast("播放列表暂无歌曲")
            } ?: showToast("播放列表为空")
        }
    }
}