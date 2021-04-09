package com.example.echo_kt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.R
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.databinding.ListItemSonglistBinding
import com.example.echo_kt.ui.main.MainFragmentDirections

class SongListAdapter(private var mList: List<SongListBean>) : RecyclerView.Adapter<ViewHolder>() {

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
    }

    override fun getItemCount(): Int = mList.size
}
class ViewHolder(private val binding : ListItemSonglistBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(item: SongListBean,index: Int) {
        binding.apply {
            binding.bean=item
        }
        binding.setClickListener {
            binding.clickListener?.let { _ ->
                val action = MainFragmentDirections.actionMainFragmentToCustomSongListFragment(index)
                it.findNavController().navigate(action)
            }
        }
    }
}
