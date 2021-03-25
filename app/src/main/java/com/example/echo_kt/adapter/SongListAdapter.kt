package com.example.echo_kt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.R
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.databinding.ListItemSonglistBinding

class SongListAdapter(private var mList: MutableLiveData<List<SongListBean>>) : RecyclerView.Adapter<ViewHolder>() {

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
        mList.value?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = mList.value!!.size
}
class ViewHolder(private val binding : ListItemSonglistBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(item: SongListBean) {
        binding.apply {
            binding.bean=item
        }
    }
    init {
        binding.setClickListener {
            binding.clickListener?.let { _ ->
                it.findNavController().navigate(R.id.action_mainFragment_to_localSongFragment)
            }
        }
    }
}
