package com.example.echo_kt.ui.search

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.example.echo_kt.R
import com.example.echo_kt.databinding.SearchFragmentBinding
import com.example.echo_kt.model.WyyMusicModel
import com.example.echo_kt.ui.search.adapter.SearchListAdapter
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
    private var source= "KUGOU"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel
        binding.apply {
            searchSrc.setOnClickListener {
                sendRequest()
            }
            etSearch.setOnKeyListener { _, keyCode, event ->
                //这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.action) {
                    //发送请求
                    sendRequest()
                    //收起软键盘
                    val imm: InputMethodManager =
                        (context?.getSystemService(Context.INPUT_METHOD_SERVICE)) as InputMethodManager
                    imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
                    return@setOnKeyListener true
                }
                false

            }
            engineLogo.setOnClickListener{
                when (source) {
                    "KUGOU" -> {
                        (it as ImageButton).setImageResource(R.mipmap.qqmusic)
                        source = "QQMUSIC"
                    }
                    "QQMUSIC" -> {
                        (it as ImageButton).setImageResource(R.mipmap.wyymusic)
                        source = "WYYMUSIC"
                    }
                    "WYYMUSIC" -> {
                        (it as ImageButton).setImageResource(R.mipmap.kugou)
                        source = "KUGOU"
                    }
                }
            }
        }
    }
    private fun sendRequest() {
        GlobalScope.launch(Dispatchers.Main) {
            val keyWord = binding.etSearch.text.toString()
            val data = withContext(Dispatchers.IO) {
                viewModel.sendRequestMessage(keyWord, source)
            }
            binding.rvSearchList.adapter = SearchListAdapter(data, source)
        }
    }

//    @SuppressLint("WrongConstant")
//    private fun initData() {
//        binding.rvFlexBox.layoutManager = FlexboxLayoutManager(context).apply {
//            flexDirection = FlexDirection.COLUMN
//            justifyContent = JustifyContent.FLEX_END
//            flexDirection = FlexDirection.ROW
//        }
//        binding.rvFlexBox.adapter= FlexBoxAdapter(
//            arrayListOf(
//                "1345",
//                "22",
//                "1",
//                "2gdfhjghkkfhdgsfdgh",
//                "1sxgsdf",
//                "2",
//                "1",
//                "2"
//            )
//        )
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}