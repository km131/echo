package com.example.echo_kt.ui.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.data.ParagraphBean
import com.example.echo_kt.databinding.ItemParagraphBinding

class ParagraphsAdapter (private var mList: MutableList<ParagraphBean>) : RecyclerView.Adapter<ParagraphsAdapter.ViewHolder>() {
    class ViewHolder(private val binding : ItemParagraphBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ParagraphBean) {
            //设置数据
            binding.apply {
                binding.info=item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemParagraphBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }
}