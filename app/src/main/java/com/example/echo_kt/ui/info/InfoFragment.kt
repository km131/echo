package com.example.echo_kt.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.echo_kt.databinding.FragmentInfoBinding
import com.example.echo_kt.ui.video.listadapter.VideoMainAdapter

class InfoFragment : Fragment() {

    private val viewModel: InfoViewModel by viewModels()
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterMain: ParagraphsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentInfoBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        adapterMain= ParagraphsAdapter(viewModel.paragraphs)
        binding.rv.adapter=adapterMain
    }

}