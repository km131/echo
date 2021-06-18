package com.example.echo_kt.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.databinding.ListItemAddSongBinding

/**
 * 多项选择列表，向歌单添加歌曲时用到
 */
class MultipleChoiceListAdapter(private var mList: MutableList<SongBean>) : RecyclerView.Adapter<MultipleChoiceListAdapter.ViewHolder>() {

    var mSelectedPositions = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemAddSongBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MultipleChoiceListAdapter.ViewHolder, position: Int) {
        holder.bind(mList[position],position)
    }

    override fun getItemCount(): Int = mList.size

    inner class ViewHolder(private val binding : ListItemAddSongBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: SongBean,i:Int) {
            //设置数据
            binding.apply {
                binding.bean=item
                executePendingBindings()
            }
            //设置监听
            binding.onClick=View.OnClickListener {
                if (isItemChecked(i)) {
                    setItemChecked(i, false)
                    binding.checkBox.isChecked = false
                } else {
                    setItemChecked(i, true)
                    binding.checkBox.isChecked = true
                }
//                notifyItemChanged(i)
            }
        }
    }

    private fun setItemChecked(position: Int, isChecked: Boolean) {
        mSelectedPositions.put(position, isChecked)
    }

    //根据位置判断条目是否选中
    private fun isItemChecked(position: Int): Boolean {
        return mSelectedPositions[position]
    }
}
