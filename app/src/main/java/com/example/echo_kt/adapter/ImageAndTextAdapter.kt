package com.example.echo_kt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.data.ImageAndTextBean
import com.example.echo_kt.databinding.ItemTextImageBinding


class ImageAndTextAdapter(private var mList: MutableList<ImageAndTextBean>) : RecyclerView.Adapter<ImageAndTextViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAndTextViewHolder {
        return ImageAndTextViewHolder(
            ItemTextImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageAndTextViewHolder, position: Int) {
        holder.bind(mList[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(holder.itemView, position)
        }
    }
    override fun getItemCount(): Int = mList.size
}
class ImageAndTextViewHolder(private val binding :ItemTextImageBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(item: ImageAndTextBean) {
        binding.apply {
            binding.bean=item
            executePendingBindings()
        }
    }
}