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
import com.example.echo_kt.ui.main.AudioBean
import kotlinx.coroutines.*

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
                    PlayerManager.instance.play(audioBean)
                    Log.i("合成提取地址", "bind: 1$audioBean")
                    }
            }
        }
    }

    private fun dataToAudioBean(
        data: SearchMusicDetails.Data,
        item: Info
    ): AudioBean {
        return AudioBean(
            name = data.song_name,
            singer = item.singerName,
            size = data.filesize.toLong(),
            duration = if (data.is_free_part != 1) data.timelength else 59000,
            path = data.play_backup_url,
            albumId = data.album_id
        )
    }

    private suspend fun requestData(item: Info): SearchMusicDetails.Data {
        Log.i("albumId=", ": ${item.albumId}")
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