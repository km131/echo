package com.example.echo_kt.ui.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.api.KuGouServer
import com.example.echo_kt.api.SearchMusicDetails
import com.example.echo_kt.data.CustomSearchBean
import com.example.echo_kt.data.Info
import com.example.echo_kt.databinding.ListItemSearchBinding
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

class SearchListAdapter internal constructor(private var mList: CustomSearchBean) :
    RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = mList.data.info.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList.data.info[position])
    }

    inner class ViewHolder internal constructor(
        private val binding: ListItemSearchBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: Info) {
            binding.apply {
                binding.data = item
                executePendingBindings()
            }
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
                    val data:SearchMusicDetails.Data = requestData(item)
                    val url: String = data.play_url
                    val songName:String  = data.audio_name
                    val download: Call<ResponseBody> = KuGouServer.create3().downloadFile(url)

                    download.enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e("ERROR", "onFailure: 请求下载失败")
                        }

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            writeResponseBodyToDisk(response.body(),songName)
                            Log.i("SUCCEED", "onResponse: 请求下载成功，")
                        }

                    })
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