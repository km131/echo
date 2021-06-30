package com.example.echo_kt.ui.video.listadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.databinding.ItemVideoMainBinding
import com.example.echo_kt.ui.video.bean.VideoBean

class VideoMainAdapter internal constructor(private var mList: ArrayList<VideoBean>) :
    RecyclerView.Adapter<VideoMainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVideoMainBinding.inflate(
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
        private val binding: ItemVideoMainBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: VideoBean) {
            binding.apply {
                binding.videoVm = item
                binding.videoTypeList.adapter = VideoMinorAdapter(item.list)
                executePendingBindings()
            }
        }

        init {
            binding.jumpLinkOnClick = View.OnClickListener {
                //TODO NOT IMPORTANT
            }
        }
    }
}