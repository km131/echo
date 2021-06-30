package com.example.echo_kt.ui.video.listadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.databinding.ListItemDoubanMinorBinding
import com.example.echo_kt.ui.video.bean.VideoBean

class VideoMinorAdapter internal constructor(private var mList: ArrayList<VideoBean.VideoMinorBean>) :
    RecyclerView.Adapter<VideoMinorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemDoubanMinorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    inner class ViewHolder internal constructor(
        private val binding: ListItemDoubanMinorBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: VideoBean.VideoMinorBean) {
            binding.apply {
                binding.videoMinorVM = item
                executePendingBindings()
            }
        }
        init {
//            binding.playOnclick= View.OnClickListener {
//                //PlayerManager.instance.getPlayList()
//                PlayerManager.instance.play(binding.pvm)
//            }
        }
    }
}