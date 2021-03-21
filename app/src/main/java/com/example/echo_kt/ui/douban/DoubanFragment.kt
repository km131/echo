package com.example.echo_kt.ui.douban

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.echo_kt.R
import com.example.echo_kt.adapter.MyAcrossAdapter
import com.example.echo_kt.adapter.MyErectAdapter
import com.example.echo_kt.databinding.DoubanFragmentBinding
import com.example.echo_kt.ui.douban.bean.DoubanBean
import com.example.echo_kt.ui.douban.listadapter.DoubanMainAdapter

class DoubanFragment : Fragment() {

    companion object {
        fun newInstance() = DoubanFragment()
    }

    private lateinit var viewModel: DoubanViewModel
    private var _binding:DoubanFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterMain:DoubanMainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= DoubanFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DoubanViewModel::class.java)
        // TODO: Use the ViewModel
        initAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        adapterMain=DoubanMainAdapter(viewModel.getDataList())
        binding.mainListDouban.adapter=adapterMain
        binding.setJumpSearch {
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_searchFragment)
        }
    }

}