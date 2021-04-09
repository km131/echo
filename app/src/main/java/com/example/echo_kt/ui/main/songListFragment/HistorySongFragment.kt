package com.example.echo_kt.ui.main.songListFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.echo_kt.ItemAdapter
import com.example.echo_kt.databinding.FragmentHistorySongBinding
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.ui.main.ListSongViewModel

class HistorySongFragment : Fragment() {

    private lateinit var viewModel: ListSongViewModel
    private var _binding: FragmentHistorySongBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistorySongBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListSongViewModel::class.java)
        binding.vm = viewModel
        binding.isNull = false
        viewModel.scanHistorySong()
        if (viewModel.listSongData!=null){
            binding.rvLocalSong.adapter = ItemAdapter(viewModel.listSongData!!)
        }else{
            binding.isNull = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    fun onClick() {
        binding.setOnClickPlayAll {
            /**
             * TODO：待完善（非空检查）
             */
            viewModel.listSongData?.let { it1 ->
                PlayList.instance.switchAudioList(it1)
            }
            PlayerManager.instance.startAll()
        }
        binding.setOnClickUpdate {
            PlayList.instance.updatePath()
        }
    }
}