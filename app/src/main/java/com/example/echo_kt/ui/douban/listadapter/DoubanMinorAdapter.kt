package com.example.echo_kt.ui.douban.listadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.databinding.ListItemDoubanMinorBinding
import com.example.echo_kt.ui.douban.bean.DoubanBean

class DoubanMinorAdapter internal constructor(private var mList: ArrayList<DoubanBean.DoubanMinorBean>) :
    RecyclerView.Adapter<DoubanMinorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemDoubanMinorBinding.inflate(
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
        private val binding: ListItemDoubanMinorBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: DoubanBean.DoubanMinorBean) {
            binding.apply {
                binding.doubanMinorVM = item
                executePendingBindings()
            }
        }
        init {
//            binding.playOnclick= View.OnClickListener {
//                //PlayerManager.instance.getPlayList()
//                PlayerManager.instance.play(binding.pvm)
//            }
        }
    }
}