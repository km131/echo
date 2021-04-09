package com.example.echo_kt.ui.main.songListFragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echo_kt.ItemAdapter
import com.example.echo_kt.R
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.databinding.CustomSongListFragmentBinding
import com.example.echo_kt.ui.SongListDialogFragment
import com.example.echo_kt.ui.main.HomeViewModel

class CustomSongListFragment : Fragment() {

    companion object {
        fun newInstance() = CustomSongListFragment()
    }

    private val viewModel: HomeViewModel by activityViewModels()
    private val args: CustomSongListFragmentArgs by navArgs()
    private var _binding: CustomSongListFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomSongListFragmentBinding.inflate(inflater,container,false)
        initToolBar()
        return binding.root
    }

    private fun initToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            toolbar.setOnMenuItemClickListener { item ->
                val b = when (item.itemId) {
                    R.id.action_rest -> {
                        onCreateDialog()
                        true
                    }
                    else -> false
                }
                b
            }
        }
    }

    private fun onCreateDialog() {
        val newFragment = SongListDialogFragment()
        // The device is using a large layout, so show the fragment as a dialog
        newFragment.show(childFragmentManager, "歌单操作对话框")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val songList:SongListBean?=viewModel.readCustomList(args.index)
        binding.songListBean = songList
        binding.rvSongList.adapter = songList?.list?.let { ItemAdapter(it) }
        binding.rvSongList.layoutManager = LinearLayoutManager(this.context)
    }

}