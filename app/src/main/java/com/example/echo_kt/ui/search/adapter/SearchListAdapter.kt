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
) : PagingDataAdapter<SearchBean, SearchListAdapter.ViewHolder>(ListDiffCallback()) {

    private lateinit var onItemClickListener: OnItemClickListener
    private var source = SourceType.KUGOU

    fun setOnItemOtherClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(binding: ListItemSearchBinding, bean: SearchBean)
    }

    fun setSource(sourceType: SourceType) {
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
        val listBody = getItem(position)!!
        holder.getBind().btnOther.setOnClickListener {
            holder.getBind().btnOther.isClickable = false
            onItemClickListener.onItemClick(holder.getBind(), listBody)
        }
        return when (source) {
            SourceType.KUGOU -> holder.bind(listBody as Info)
            SourceType.QQMUSIC -> holder.bind(listBody as AudioList, qqMusicServer)
            SourceType.WYYMUSIC -> holder.bind(listBody as WyySearchListBean.Result.Song,
                wyyMusicServer
            )
            SourceType.MIGUMUSIC -> holder.bind(
                listBody as MiguSearchListBean.SongResultData.ResultXX,
                miguMusicServer
            )
        }
    }

    class ViewHolder(
        private val binding: ListItemSearchBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun getBind(): ListItemSearchBinding {
            return binding
        }

        //????????????
        fun bind(item: Info) {
            binding.bean = ShowSearchBean(item.songName, item.singerName)
            binding.llPlay.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    val data = withContext(Dispatchers.IO) {
                        KuGouModel.getMusicBean(item.albumId, item.hash)
                    }
                    data?.run {
                        val audioBean = KuGouModel.convertSongBean(item, this.img, this.play_url,this.lyrics)
                        PlayerManager.instance.playNewAudio(audioBean)
                    } ?: showToast("????????????????????????????????????????????????")
                }
            }
        }

        //qq??????
        fun bind(item: AudioList, qqMusicServer: QQMusicServer) {
            binding.apply {
                val model = QQMusicModel(qqMusicServer)
                var authorStr = ""
                for (i in item.singer.indices) {
                    authorStr += item.singer[i].singerName + "/"
                }
                authorStr = authorStr.substring(0,authorStr.length-1)
                binding.bean = ShowSearchBean(item.songName, authorStr)
                binding.llPlay.setOnClickListener {
                    GlobalScope.launch(Dispatchers.Main) {
                        val vk = model.getVKey(item.songmid)
                        vk?.let {
                            val url =
                                "https://ws.stream.qqmusic.qq.com/${vk.req.midurlinfo.reqData[0].purl}"
                            val data = model.getAudioFile(url)
                            data.enqueue(object : Callback<ResponseBody> {
                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Log.e("SearchLIstAdapter:147", "onFailure: ????????????")
                                }

                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: Response<ResponseBody>
                                ) {
                                    val parameterMap = HashMap<String, String>()
                                    parameterMap["mid"] = item.songmid
                                    PlayerManager.instance.playNewAudio(
                                        QQMusicModel.convertSongBean(
                                            item,
                                            url,
                                            parameterMap
                                        )
                                    )
                                    Log.i("SearchLIstAdapter:163", "onResponse: ???????????????")
                                }

                            })
                        } ?: showToast("?????????????????????????????????????????????")
                    }
                }
            }
        }

        //???????????????
        fun bind(item: WyySearchListBean.Result.Song, wyyMusicServer: WyyMusicServer) {
            val viewModel = WyyMusicModel(wyyMusicServer)
            var authorStr = ""
            for (i in item.author.indices) {
                authorStr += item.author[i].name + "/"
            }
            authorStr = authorStr.substring(0,authorStr.length-1)
            binding.apply {
                binding.bean = ShowSearchBean(item.name, authorStr)
                binding.llPlay.setOnClickListener {
                    GlobalScope.launch(Dispatchers.Main) {
                        val response = viewModel.getSongPath(item.mid)
                        response?.let {
                            val audioBean: SongBean =
                                WyyMusicModel.convertSongBean(response.data[0], item)
                            PlayerManager.instance.playNewAudio(audioBean)
                        } ?: showToast("?????????????????????????????????????????????")
                    }
                }
            }
        }

        //????????????
        fun bind(item: MiguSearchListBean.SongResultData.ResultXX, server: MiguMusicServer) {
            val viewModel = MiGuMusicModel(server)
            binding.apply {
                binding.bean = ShowSearchBean(item.songName, item.singer)
                binding.llPlay.setOnClickListener {
                    GlobalScope.launch(Dispatchers.Main) {
                        val toneFlag = "HQ"
                        val response = viewModel.getMusicBean(
                            albumId = item.albumId,
                            songId = item.songId,
                            toneFlag = toneFlag
                        )
                        response?.let {
                            val audioBean: SongBean =
                                MiGuMusicModel.convertSongBean(response)
                            PlayerManager.instance.playNewAudio(audioBean)
                        } ?: showToast("?????????????????????????????????????????????")
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
