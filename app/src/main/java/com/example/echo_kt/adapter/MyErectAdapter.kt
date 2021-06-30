package com.example.echo_kt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.data.ErectBean
import com.example.echo_kt.databinding.ItemTextImageBinding


class MyErectAdapter(private var mList: MutableList<ErectBean>) : RecyclerView.Adapter<ErectViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ErectViewHolder {
        return ErectViewHolder(
            ItemTextImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ErectViewHolder, position: Int) {
        holder.bind(mList[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(holder.itemView, position)
        }
    }
    override fun getItemCount(): Int = mList.size
}
class ErectViewHolder(private val binding :ItemTextImageBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(item: ErectBean) {
        binding.apply {
            binding.erectBean=item
            executePendingBindings()
        }
    }
}
