package com.example.echo_kt.ui.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.api.kugou.Info
import com.example.echo_kt.api.kugou.KuGouServer
import com.example.echo_kt.api.migu.MiguMusicServer
import com.example.echo_kt.api.migu.MiguSearchListBean
import com.example.echo_kt.api.qqmusic.AudioList
import com.example.echo_kt.api.qqmusic.QQMusicServer
import com.example.echo_kt.api.showToast
import com.example.echo_kt.api.wyymusic.WyyMusicServer
import com.example.echo_kt.api.wyymusic.WyySearchListBean
import com.example.echo_kt.data.SearchBean
import com.example.echo_kt.data.ShowSearchBean
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.databinding.ListItemSearchBinding
import com.example.echo_kt.model.KuGouModel
import com.example.echo_kt.model.MiGuMusicModel
import com.example.echo_kt.model.QQMusicModel
import com.example.echo_kt.model.WyyMusicModel
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.ui.SourceType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchListAdapter constructor(
    private val kuGouServer: KuGouServer,
    private val qqMusicServer: QQMusicServer,
    private val wyyMusicServer: WyyMusicServer,
    private val miguMusicServer: MiguMusicServer
)  : PagingDataAdapter<SearchBean, SearchListAdapter.ViewHolder>(ListDiffCallback()) {

    private lateinit var onItemClickListener: OnItemClickListener
    private var source=SourceType.KUGOU

    fun setOnItemOtherClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
    interface OnItemClickListener{
        fun onItemClick(binding: ListItemSearchBinding,bean: SearchBean)
    }
    fun setSource(sourceType: SourceType){
        source = sourceType
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val  listBody = getItem(position)!!
        holder.getBind().btnOther.setOnClickListener {
            holder.getBind().btnOther.isClickable = false
            onItemClickListener.onItemClick(holder.getBind(),listBody)
        }
        return when(source){
            SourceType.KUGOU -> holder.bind(listBody as Info)
            SourceType.QQMUSIC -> holder.bind(listBody as AudioList,qqMusicServer)
            SourceType.WYYMUSIC -> holder.bind(listBody as WyySearchListBean.Result.Song,wyyMusicServer)
            SourceType.MIGUMUSIC -> holder.bind(listBody as MiguSearchListBean.SongResultData.ResultXX,miguMusicServer)
        }
    }

    class ViewHolder (
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
                        KuGouModel.getMusicBean(item.albumId,item.hash)
                    }
                    data?.let {
                        val audioBean = KuGouModel.convertSongBean(item,it.img,it.play_url)
                        PlayerManager.instance.playNewAudio(audioBean)
                    } ?: showToast("网络出问题了，也可能是接口挂了")
                }
            }
//            //酷狗单曲下载（其他的没写）
//            binding.btnOther.setOnClickListener{
//                GlobalScope.launch(Dispatchers.Main) {
//                    val data: SearchMusicDetails.Data? = withContext(Dispatchers.IO) {
//                        KuGouModel(kuGouServer).getMusicBean(item.albumId,item.hash)
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
        fun bind(item: AudioList,qqMusicServer: QQMusicServer) {
            binding.apply {
                binding.bean = ShowSearchBean(item.songName, item.singer[0].singerName)
                val model = QQMusicModel(qqMusicServer)
                binding.llPlay.setOnClickListener {
                    GlobalScope.launch(Dispatchers.Main) {
                        val vk = model.getVKey(item.songmid)
                        vk?.let {
                            val url =
                                "https://ws.stream.qqmusic.qq.com/${vk.req.midurlinfo.reqData[0].purl}"
                            val data = model.getAudioFile(url)
                            data.enqueue(object : Callback<ResponseBody> {
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Log.e("SearchLIstAdapter:147", "onFailure: 请求失败")
                                }

                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: Response<ResponseBody>
                                ) {
                                    val parameterMap = HashMap<String,String>()
                                    parameterMap["mid"]=item.songmid
                                    PlayerManager.instance.playNewAudio(
                                        QQMusicModel.convertSongBean(
                                            item,
                                            url,
                                            parameterMap
                                        )
                                    )
                                    Log.i("SearchLIstAdapter:163", "onResponse: 请求成功，")
                                }

                            })
                        } ?: showToast("网络出问题了，也可能是接口挂了")
                    }
                }
            }
        }

        //网易云音乐
        fun bind(item: WyySearchListBean.Result.Song,wyyMusicServer: WyyMusicServer) {
            val viewModel = WyyMusicModel(wyyMusicServer)
            binding.apply {
                binding.bean = ShowSearchBean(item.name, item.author[0].name)
                binding.llPlay.setOnClickListener {
                    GlobalScope.launch(Dispatchers.Main) {
                        val response = viewModel.getSongPath(item.mid)
                        response?.let {
                            val audioBean: SongBean =
                                WyyMusicModel.convertSongBean(response.data[0], item)
                            PlayerManager.instance.playNewAudio(audioBean)
                        } ?: showToast("网络出问题了，也可能是接口挂了")
                    }
                }
            }
        }
        //咪咕音乐
        fun bind(item: MiguSearchListBean.SongResultData.ResultXX,server: MiguMusicServer) {
            val viewModel = MiGuMusicModel(server)
            binding.apply {
                binding.bean = ShowSearchBean(item.songName, item.singer)
                binding.llPlay.setOnClickListener {
                    GlobalScope.launch(Dispatchers.Main) {
                        val toneFlag = "HQ"
                        val response = viewModel.getMusicBean(albumId = item.albumId ,songId=item.songId,toneFlag=toneFlag)
                        response?.let {
                            val audioBean: SongBean =
                                MiGuMusicModel.convertSongBean(response)
                            PlayerManager.instance.playNewAudio(audioBean)
                        } ?: showToast("网络出问题了，也可能是接口挂了")
                    }
                }
            }
        }
    }
}
private class ListDiffCallback : DiffUtil.ItemCallback<SearchBean>() {
    override fun areItemsTheSame(oldItem: SearchBean, newItem: SearchBean): Boolean {
       return oldItem.getId() == newItem.getId()
    }

    override fun areContentsTheSame(oldItem: SearchBean, newItem: SearchBean): Boolean {
       return oldItem.getId() == newItem.getId()
    }
}
