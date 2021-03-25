package com.example.echo_kt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.R
import com.example.echo_kt.data.ErectBean
import com.example.echo_kt.databinding.ListItemWork1Binding

class MyErectAdapter(private var mList: MutableList<ErectBean>) : RecyclerView.Adapter<ErectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ErectViewHolder {
        return ErectViewHolder(
            ListItemWork1Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ErectViewHolder, position: Int) {
       holder.bind(mList[position])
    }

    override fun getItemCount(): Int = mList.size
}
class ErectViewHolder(private val binding :ListItemWork1Binding): RecyclerView.ViewHolder(binding.root){
    fun bind(item: ErectBean) {
        binding.apply {
            binding.vmWork1=item
            executePendingBindings()
        }
    }
    init {
        binding.setClickListener {
            binding.vmWork1?.let { _ ->
                it.findNavController().navigate(R.id.action_mainFragment_to_localSongFragment)
            }
        }
    }
}
