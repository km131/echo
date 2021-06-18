package com.example.echo_kt.ui.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.echo_kt.R
import com.example.echo_kt.adapter.SongViewModel
import com.example.echo_kt.api.migu.MiguSearchListBean
import com.example.echo_kt.api.qqmusic.ListSearchResponse
import com.example.echo_kt.api.showToast
import com.example.echo_kt.api.wyymusic.WyySearchListBean
import com.example.echo_kt.api.kugou.KuGouSearchBean
import com.example.echo_kt.data.SearchBean
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.databinding.ListItemSearchBinding
import com.example.echo_kt.databinding.SearchFragmentBinding
import com.example.echo_kt.model.KUGOUModel
import com.example.echo_kt.model.MiGuMusicModel
import com.example.echo_kt.model.QQMusicModel
import com.example.echo_kt.model.WyyMusicModel
import com.example.echo_kt.ui.search.adapter.SearchListAdapter
import kotlinx.coroutines.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val viewModel: SearchViewModel by activityViewModels()
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
                        (it as ImageButton).setImageResource(R.mipmap.migu)
                        source = "MIGUMUSIC"
                    }
                    "MIGUMUSIC" -> {
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
            if (keyWord.trim().isNotEmpty()){
                val data = viewModel.sendRequestMessage(keyWord, source)
                data?.let {
                    binding.rvSearchList.adapter = SearchListAdapter(data, source).apply {
                        setOnItemClickListener(object : SearchListAdapter.OnItemClickListener{
                            override fun onItemClick(binding: ListItemSearchBinding, position: Int) {
                                val vm: SongViewModel by activityViewModels()
                                GlobalScope.launch(Dispatchers.Main) {
                                    val audioBean = withContext(Dispatchers.Main) {
                                        convertToAudioBean(data, source, position)
                                    }
                                    audioBean?.let {
                                        vm.audioBean.set(audioBean)
                                        findNavController().navigate(R.id.action_searchFragment_to_bottomDialogFragment)
                                        binding.btnOther.isClickable = true
                                    } ?: showToast("未拿到歌曲信息，请返回上个页面")
                                }
                            }
                        })
                    }
                } ?: showToast("网络出问题了，也可能是接口挂了")
            } else showToast("此BUG已被处理")
        }
    }

    private suspend fun convertToAudioBean(
        data: SearchBean,
        course: String,
        position: Int
    ): SongBean? {
        when (course) {
            "KUGOU" -> {
                val s = (data as KuGouSearchBean).data.info[position]
                val dat = withContext(Dispatchers.IO) {
                    KUGOUModel().getMusicBean(s.albumId,s.hash)
                }
                return KUGOUModel().convertSongBean(s,dat!!.img,dat.play_url)
            }
            "QQMUSIC" -> {
                val s = (data as ListSearchResponse).data.songList.data[position]
                val url = QQMusicModel().getPath(s.songmid)
                val parameterMap = HashMap<String, String>()
                parameterMap["mid"] = s.songmid
                return QQMusicModel().convertSongBean(s, url, parameterMap)
            }
            "WYYMUSIC" -> {
                val s= (data as WyySearchListBean).result.songs[position]
                val response = WyyMusicModel().getSongPath(s.id)
                response?.let {
                    return WyyMusicModel().convertSongBean(response.data[0], s)
                }
            }
            "MIGUMUSIC" ->{
                val s=(data as MiguSearchListBean).songResultData.result[position]
                val toneFlag = "HQ"
                val response = MiGuMusicModel().getMusicBean(s.albumId ,s.songId,toneFlag)
                response?.let {
                   return MiGuMusicModel().convertSongBean(response)
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
        _binding=null
    }

}