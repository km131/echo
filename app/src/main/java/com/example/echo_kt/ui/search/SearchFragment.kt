package com.example.echo_kt.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.echo_kt.R
import com.example.echo_kt.adapter.SongViewModel
import com.example.echo_kt.api.kugou.Info
import com.example.echo_kt.api.migu.MiguSearchListBean
import com.example.echo_kt.api.qqmusic.AudioList
import com.example.echo_kt.api.showToast
import com.example.echo_kt.api.wyymusic.WyySearchListBean
import com.example.echo_kt.data.SearchBean
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.databinding.ListItemSearchBinding
import com.example.echo_kt.databinding.SearchFragmentBinding
import com.example.echo_kt.model.KuGouModel
import com.example.echo_kt.model.MiGuMusicModel
import com.example.echo_kt.model.QQMusicModel
import com.example.echo_kt.model.WyyMusicModel
import com.example.echo_kt.ui.SourceType
import com.example.echo_kt.ui.search.adapter.SearchListAdapter
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by activityViewModels()
    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private var searchJob: Job? = null
    private lateinit var adapter: SearchListAdapter

    //当前搜索的数据来源和上次搜索的数据来源，在检测到来源切换时，清空列表中之前的数据
    private var currentSource = SourceType.KUGOU
    private var lastSource = SourceType.KUGOU

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.apply {
            searchSrc.setOnClickListener {
                val keyWord = binding.etSearch.text.toString()
                sendRequest(keyWord)
            }
            etSearch.setOnKeyListener { _, keyCode, event ->
                //这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.action) {
                    //发送请求
                    val keyWord = binding.etSearch.text.toString()
                    sendRequest(keyWord)
                    //收起软键盘
                    val imm: InputMethodManager =
                        (context?.getSystemService(Context.INPUT_METHOD_SERVICE)) as InputMethodManager
                    imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
                    return@setOnKeyListener true
                }
                false

            }
            resetListAdapter()
            rvSearchList.adapter = adapter
            engineLogo.setOnClickListener {
                when (currentSource) {
                    SourceType.KUGOU -> {
                        (it as ShapeableImageView).setImageResource(R.mipmap.qqmusic)
                        currentSource = SourceType.QQMUSIC
                    }
                    SourceType.QQMUSIC -> {
                        (it as ShapeableImageView).setImageResource(R.mipmap.wyymusic)
                        currentSource = SourceType.WYYMUSIC
                    }
                    SourceType.WYYMUSIC -> {
                        (it as ShapeableImageView).setImageResource(R.mipmap.migu)
                        currentSource = SourceType.MIGUMUSIC
                    }
                    SourceType.MIGUMUSIC -> {
                        (it as ShapeableImageView).setImageResource(R.mipmap.kugou)
                        currentSource = SourceType.KUGOU
                    }
                }
            }
        }
    }

    private fun sendRequest(keyWord: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            Log.d("SearchFragment：当前来源", "$currentSource ==== 上次来源$lastSource")
            if ( currentSource != lastSource){
                resetListAdapter()
                lastSource=currentSource
            }
            viewModel.sendRequestMessage(keyWord, currentSource).collectLatest {
                adapter.setSource(currentSource)
                adapter.submitData(it)
                lastSource=currentSource
            }
        }
    }

    private suspend fun convertToAudioBean(
        data: SearchBean,
        course: SourceType,
    ): SongBean? {
        when (course) {
            SourceType.KUGOU -> {
                val s = (data as Info)
                val dat = withContext(Dispatchers.IO) {
                    KuGouModel.getMusicBean(s.albumId, s.hash)
                }
                return KuGouModel.convertSongBean(s, dat!!.img, dat.play_url)
            }
            SourceType.QQMUSIC -> {
                val s = (data as AudioList)
                val url = QQMusicModel(viewModel.qqMusicServer).getPath(s.songmid)
                val parameterMap = HashMap<String, String>()
                parameterMap["mid"] = s.songmid
                return QQMusicModel.convertSongBean(s, url, parameterMap)
            }
            SourceType.WYYMUSIC -> {
                val s = (data as WyySearchListBean.Result.Song)
                val response = WyyMusicModel(viewModel.wyyMusicServer).getSongPath(s.mid)
                response?.let {
                    return WyyMusicModel.convertSongBean(response.data[0], s)
                }
            }
            SourceType.MIGUMUSIC -> {
                val s = (data as MiguSearchListBean.SongResultData.ResultXX)
                val toneFlag = "HQ"
                val response = MiGuMusicModel(viewModel.miGuMusicServer).getMusicBean(
                    s.albumId,
                    s.songId,
                    toneFlag
                )
                response?.let {
                    return MiGuMusicModel.convertSongBean(response)
                }
            }
        }
        return null
    }
//    历史搜索
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
        searchJob?.cancel()
        _binding = null
    }


    private fun resetListAdapter() {
        adapter = SearchListAdapter(
            viewModel.kuGouServer,
            viewModel.qqMusicServer,
            viewModel.wyyMusicServer,
            viewModel.miGuMusicServer
        ).apply {
            setOnItemOtherClickListener(object : SearchListAdapter.OnItemClickListener {
                override fun onItemClick(binding: ListItemSearchBinding, bean: SearchBean) {
                    val vm: SongViewModel by activityViewModels()
                    lifecycleScope.launch(Dispatchers.Main) {
                        val audioBean = withContext(Dispatchers.Main) {
                            // 此处传上一次使用的来源，如使用当前来源的话，在获取列表后切换来源
                            // 然后拿取详细信息时会因来源不同而崩溃
                            convertToAudioBean(bean, lastSource)
                        }
                        audioBean?.let {
                            vm.audioBean.set(audioBean)
                            findNavController().navigate(R.id.action_searchFragment_to_bottomDialogFragment)
                            binding.btnOther.isClickable = true
                        } ?: showToast("未拿到歌曲信息")
                    }
                }
            })
        }
        binding.rvSearchList.adapter = adapter
    }

}