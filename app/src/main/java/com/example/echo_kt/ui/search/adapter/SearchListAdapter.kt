package com.example.echo_kt.ui.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.api.KuGouServer
import com.example.echo_kt.api.SearchMusicDetails
import com.example.echo_kt.api.migu.MiguSearchListBean
import com.example.echo_kt.api.migu.MiguSearchMusicBean
import com.example.echo_kt.api.qqmusic.AudioList
import com.example.echo_kt.api.qqmusic.ListSearchResponse
import com.example.echo_kt.api.showToast
import com.example.echo_kt.api.wyymusic.WyySearchListBean
import com.example.echo_kt.data.*
import com.example.echo_kt.databinding.ListItemSearchBinding
import com.example.echo_kt.model.KUGOUModel
import com.example.echo_kt.model.MiGuMusicModel
import com.example.echo_kt.model.QQMusicModel
import com.example.echo_kt.model.WyyMusicModel
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.util.dataToAudioBean
import com.example.echo_kt.util.writeResponseBodyToDisk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchListAdapter internal constructor(private var mList: SearchBean,private var cource: String) :
    RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    val list = when(cource){
        "KUGOU" -> mList as CustomSearchBean
        "QQMUSIC" -> mList as ListSearchResponse
        "WYYMUSIC" -> mList as WyySearchListBean
        "MIGUMUSIC" -> mList as MiguSearchListBean
        else -> mList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int{
        return when(cource){
            "KUGOU" -> (mList as CustomSearchBean).data.info.size
            "QQMUSIC" -> (mList as ListSearchResponse).data.songList.data.size
            "WYYMUSIC" -> (mList as WyySearchListBean).result.songs.size
            "MIGUMUSIC" -> (mList as MiguSearchListBean).songResultData.result.size
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getBind().btnOther.setOnClickListener {
            onItemClickListener.onItemClick(holder.itemView, position)
        }
        return when(cource){
            "KUGOU" -> holder.bind((mList as CustomSearchBean).data.info[position])
            "QQMUSIC" -> holder.bind((mList as ListSearchResponse).data.songList.data[position])
            "WYYMUSIC" -> holder.bind((mList as WyySearchListBean).result.songs[position])
            "MIGUMUSIC" -> holder.bind((mList as MiguSearchListBean).songResultData.result[position])
            else -> holder.bind((mList as CustomSearchBean).data.info[position])
        }
    }

    inner class ViewHolder internal constructor(
        private val binding: ListItemSearchBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun getBind():ListItemSearchBinding{
            return binding
        }
        //酷狗音乐
        fun bind(item: Info) {
            binding.bean = ShowSearchBean(item.songName, item.singerName)
            binding.llPlay.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    val data = withContext(Dispatchers.IO) {
                        KUGOUModel().getMusicBean(item)
                    }
                    data?.let {
                        Log.i("data=", ":$data")
                        val audioBean = dataToAudioBean(data, item)
                        PlayerManager.instance.playNewAudio(audioBean)
                        Log.i("合成", "audioBean: $audioBean")
                    } ?: showToast("网络出问题了，也可能是接口挂了")
                }
            }
            //酷狗单曲下载（其他的没写）
//            binding.btnOther.setOnClickListener{
//                GlobalScope.launch(Dispatchers.Main) {
//                    val data: SearchMusicDetails.Data? = withContext(Dispatchers.IO) {
//                        KUGOUModel().getMusicBean(item)
//                    }
//                    data?.let {
//                        val url: String = data.play_url
//                        val songName: String = data.audio_name
//                        val download: Call<ResponseBody> = KuGouServer.create3().downloadFile(url)
//
//                        download.enqueue(object : Callback<ResponseBody> {
//                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                                Log.e("ERROR", "onFailure: 请求下载失败")
//                            }
//
//                            override fun onResponse(
//                                call: Call<ResponseBody>,
//                                response: Response<ResponseBody>
//                            ) {
//                                writeResponseBodyToDisk(response.body(), songName)
//                                Log.i("SUCCEED", "onResponse: 请求下载成功，")
//                            }
//                        })
//                    } ?: showToast("网络出问题了，也可能是接口挂了")
//                }
//            }
        }
        //qq音乐
        fun bind(item: AudioList) {
            binding.apply {
                binding.bean = ShowSearchBean(item.songName, item.singer[0].singerName)
                val model = QQMusicModel()
                binding.llPlay.setOnClickListener {
                    GlobalScope.launch(Dispatchers.Main) {
                        val vk = model.getVKey(item.mediaMid)
                        vk?.let {
                            val url =
                                "https://ws.stream.qqmusic.qq.com/${vk.req.midurlinfo.reqData[0].purl}"
                            val data = model.getAudioFile(url)
                            data.enqueue(object : Callback<ResponseBody> {
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Log.e("SearchLIstAdapter:153", "onFailure: 请求失败")
                                }

                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: Response<ResponseBody>
                                ) {
                                    PlayerManager.instance.playNewAudio(
                                        model.convertAudioBean(
                                            item,
                                            url
                                        )
                                    )
                                    Log.i("SearchLIstAdapter:153", "onResponse: 请求成功，")
                                }

                            })
                        } ?: showToast("网络出问题了，也可能是接口挂了")
                    }
                }
            }
        }

        //网易云音乐
        fun bind(item: WyySearchListBean.Result.Song) {
            val viewModel = WyyMusicModel()
            binding.apply {
                binding.bean = ShowSearchBean(item.name, item.author[0].name)
                binding.llPlay.setOnClickListener {
                    GlobalScope.launch(Dispatchers.Main) {
                        val response = viewModel.getSongPath(item.id)
                        response?.let {
                            val audioBean: AudioBean =
                                viewModel.convertAudioBean(response.data[0], item)
                            PlayerManager.instance.playNewAudio(audioBean)
                        } ?: showToast("网络出问题了，也可能是接口挂了")
                    }
                }
            }
        }
        //咪咕音乐
        fun bind(item: MiguSearchListBean.SongResultData.ResultXX) {
            val viewModel = MiGuMusicModel()
            binding.apply {
                binding.bean = ShowSearchBean(item.songName, item.singer)
                binding.llPlay.setOnClickListener {
                    GlobalScope.launch(Dispatchers.Main) {
                        val toneFlag = "HQ"
                        val response = viewModel.getMusicBean(albumId = item.albumId ,songId=item.songId,toneFlag=toneFlag)
                        response?.let {
                            val audioBean: AudioBean =
                                viewModel.convertAudioBean(response)
                            PlayerManager.instance.playNewAudio(audioBean)
                        } ?: showToast("网络出问题了，也可能是接口挂了")
                    }
                }
            }
        }
    }
}