package com.example.echo_kt.ui.douban.listadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.databinding.ListItemDoubanMainBinding
import com.example.echo_kt.ui.douban.bean.DoubanBean

class DoubanMainAdapter internal constructor(private var mList: ArrayList<DoubanBean>) :
    RecyclerView.Adapter<DoubanMainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemDoubanMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    inner class ViewHolder internal constructor(
        private val binding: ListItemDoubanMainBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: DoubanBean) {
            binding.apply {
                binding.doubanMainVM = item
                binding.videoTypeList.adapter = DoubanMinorAdapter(item.list)
                executePendingBindings()
            }
        }

        init {
            binding.jumpLinkOnClick = View.OnClickListener {
                //TODO NOT IMPORTANT
            }
        }
    }
}