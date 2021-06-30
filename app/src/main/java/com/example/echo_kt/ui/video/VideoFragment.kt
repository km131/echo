package com.example.echo_kt.ui.video

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.echo_kt.R
import com.example.echo_kt.databinding.VideoFragmentBinding
import com.example.echo_kt.ui.video.listadapter.VideoMainAdapter

class VideoFragment : Fragment() {

    companion object {
        fun newInstance() = VideoFragment()
    }

    private lateinit var viewModel: VideoViewModel
    private var _binding:VideoFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterMain:VideoMainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= VideoFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VideoViewModel::class.java)
        // TODO: Use the ViewModel
        initAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        adapterMain=VideoMainAdapter(viewModel.getDataList())
        binding.mainListVideo.adapter=adapterMain
        binding.setJumpSearch {
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_searchFragment)
        }
    }

}