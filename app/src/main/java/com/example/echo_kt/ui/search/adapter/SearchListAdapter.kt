package com.example.echo_kt.ui.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.api.KuGouServer
import com.example.echo_kt.api.SearchMusicDetails
import com.example.echo_kt.api.qqmusic.AudioList
import com.example.echo_kt.api.qqmusic.ListSearchResponse
import com.example.echo_kt.api.wyymusic.WyySearchListBean
import com.example.echo_kt.data.*
import com.example.echo_kt.databinding.ListItemSearchBinding
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
    val list = when(cource){
        "KUGOU" -> mList as CustomSearchBean
        "QQMUSIC" -> mList as ListSearchResponse
        "WYYMUSIC" -> mList as WyySearchListBean
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
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return when(cource){
            "KUGOU" -> holder.bind((mList as CustomSearchBean).data.info[position])
            "QQMUSIC" -> holder.bind((mList as ListSearchResponse).data.songList.data[position])
            "WYYMUSIC" -> holder.bind((mList as WyySearchListBean).result.songs[position])
            else -> holder.bind((mList as CustomSearchBean).data.info[position])
        }
    }

    inner class ViewHolder internal constructor(
        private val binding: ListItemSearchBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        //酷狗音乐
        fun bind(item: Info) {
            binding.bean = ShowSearchBean(item.songName, item.singerName)
            binding.onClick = View.OnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    val data = requestData(item)
                    Log.i("data=", ":$data")
                    val audioBean = dataToAudioBean(data, item)
                    PlayerManager.instance.playNewAudio(audioBean)
                    Log.i("合成提取地址", "bind: 1$audioBean")
                }
            }
            binding.downloadOnClick = View.OnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    val data: SearchMusicDetails.Data = requestData(item)
                    val url: String = data.play_url
                    val songName: String = data.audio_name
                    val download: Call<ResponseBody> = KuGouServer.create3().downloadFile(url)

                    download.enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e("ERROR", "onFailure: 请求下载失败")
                        }

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            writeResponseBodyToDisk(response.body(), songName)
                            Log.i("SUCCEED", "onResponse: 请求下载成功，")
                        }

                    })
                }
            }
        }

        //qq音乐
        fun bind(item: AudioList) {
            binding.apply {
                binding.bean = ShowSearchBean(item.songName, item.singer[0].singerName)
                val model = QQMusicModel()
                onClick = View.OnClickListener {
                    GlobalScope.launch(Dispatchers.Main) {
                        val url =
                            "https://ws.stream.qqmusic.qq.com/${model.getVKey(item.mediaMid).req.midurlinfo.reqData[0].purl}"
                        val data = model.getAudioFile(url)
                        data.enqueue(object : Callback<ResponseBody> {
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Log.e("ERROR", "onFailure: 请求下载失败")
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
                                Log.i("SUCCEED", "onResponse: 请求下载成功，")
                            }

                        })
                    }
                }
            }
        }
        //网易云音乐
        fun bind(item:WyySearchListBean.Result.Song){
            val viewModel: WyyMusicModel = WyyMusicModel()
            binding.apply {
                binding.bean = ShowSearchBean(item.name, item.author[0].name)
                onClick = View.OnClickListener {
                    GlobalScope.launch(Dispatchers.Main) {
                        val audioBean: AudioBean =
                            viewModel.convertAudioBean(viewModel.getSongPath(item.id).data[0], item)
                        PlayerManager.instance.playNewAudio(audioBean)
                    }
                }
            }
        }
    }

    private suspend fun requestData(item: Info): SearchMusicDetails.Data {
        Log.i("albumId=", ": ${item.albumId}")
        //参数aid请求时可能会返回空值，但请求数据时不能为空，故改为0
        var aid = "0"
        if (item.albumId != "")
            aid = item.albumId
        return withContext(Dispatchers.IO) {
            KuGouServer.create2().searchMusic(
                aid = aid,
                hash = item.hash
            ).data
        }
    }

}