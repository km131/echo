package com.example.echo_kt.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.databinding.ListItemSonglistBinding
import com.example.echo_kt.util.getRandomColor
import java.util.*

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
        mList[position].let { holder.bind(it,position) }
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
    fun bind(item: SongListBean,position: Int) {
        binding.apply {
            binding.bean = item
            binding.albumView.text = (position+1).toString()
            val rgb = getRandomColor()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.albumView.solid = Color.rgb(rgb[0],rgb[1],rgb[2])
                binding.albumView.corner=20f
            }
        }
    }
}
