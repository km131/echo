package com.example.echo_kt.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.databinding.ItemFlexStringBinding

/**
 * 目前暂只支持纯文字
 */

class FlexBoxAdapter internal constructor(private var mList: ArrayList<String>)
    : RecyclerView.Adapter<FlexBoxAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(ItemFlexStringBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = mList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    inner class ViewHolder internal constructor(
        private val binding: ItemFlexStringBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.apply {
                binding.data = item
                Log.i("TAG", "bind: " + item)
                executePendingBindings()
            }
        }
    }
}
