package com.example.echo_kt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.databinding.ListItemSonglistBinding

/**
 * 歌单列表，在HomeFragment中
 */
class SongListAdapter(private var mList: List<SongListBean>) : RecyclerView.Adapter<ViewHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemSonglistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mList[position].let { holder.bind(it) }
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(holder.itemView, position)
        }
        holder.itemView.setOnLongClickListener {
            onItemClickListener.onItemLongClick(holder.itemView, position)
            true
        }
    }
    override fun getItemCount(): Int = mList.size
}
class ViewHolder(private val binding: ListItemSonglistBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SongListBean) {
        binding.apply {
            binding.bean = item
        }
    }
}
