package com.example.echo_kt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.data.AcrossBean
import com.example.echo_kt.databinding.ListItemDoubanMainBindingImpl
import com.example.echo_kt.databinding.ListItemWork2Binding

class MyAcrossAdapter(private var mList: MutableList<AcrossBean>) : RecyclerView.Adapter<AcrossViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcrossViewHolder {
        return AcrossViewHolder(
            ListItemWork2Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AcrossViewHolder, position: Int) {
       holder.bind(mList[position])
    }

    override fun getItemCount(): Int = mList.size
}
class AcrossViewHolder(private val binding : ListItemWork2Binding): RecyclerView.ViewHolder(binding.root){
    fun bind(item: AcrossBean) {
        binding.apply {
            binding.vmWork2=item
            executePendingBindings()
        }
    }
}
