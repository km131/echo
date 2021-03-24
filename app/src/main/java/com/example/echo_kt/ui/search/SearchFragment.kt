package com.example.echo_kt.ui.search

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.echo_kt.data.CustomSearchBean
import com.example.echo_kt.databinding.SearchFragmentBinding
import com.example.echo_kt.ui.FlexBoxAdapter
import com.example.echo_kt.ui.search.adapter.SearchListAdapter
import com.google.android.flexbox.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private var _binding:SearchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel
        initData()
        binding.searchSrc.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                val keyWord= binding.etSearch.text.toString()
                val data = withContext(Dispatchers.IO){
                    viewModel.sendRequestMessage(keyWord)
                }
                binding.rvSearchList.adapter= SearchListAdapter(data)
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun initData() {
        binding.rvFlexBox.layoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.COLUMN
            justifyContent = JustifyContent.FLEX_END
            flexDirection = FlexDirection.ROW
        }
        binding.rvFlexBox.adapter= FlexBoxAdapter(
            arrayListOf(
                "1345",
                "22",
                "1",
                "2gdfhjghkkfhdgsfdgh",
                "1sxgsdf",
                "2",
                "1",
                "2"
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}