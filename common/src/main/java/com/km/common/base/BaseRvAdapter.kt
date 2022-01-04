package com.km.common.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.km.common.base.BaseRvAdapter.BaseViewHolder

abstract class BaseRvAdapter<T,B : ViewDataBinding>(context: Context, data: MutableList<T>?) :
    RecyclerView.Adapter<BaseViewHolder<B>>() {
    private var mDatas: MutableList<T> = data ?: mutableListOf()
    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    protected var mItemClickListener: ((view: View, position:Int, data:T )->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<B> {
        val mBinding: B = DataBindingUtil.inflate(mInflater, getItemViewId(viewType), parent, false)
        val holder: BaseViewHolder<B> = BaseViewHolder(mBinding)
        mItemClickListener?.let {
            holder.itemView.setOnClickListener { v ->
              it.invoke(v, holder.layoutPosition, mDatas[holder.adapterPosition])
            }
        }
        return holder
    }

    abstract fun getItemViewId(viewType: Int): Int

    override fun onBindViewHolder(
        holder: BaseViewHolder<B>,
        position: Int
    ) {
        bindData(holder.binding, position, mDatas[position])
    }
    override fun getItemCount(): Int {
        return mDatas.size
    }
    abstract fun bindData(mBinding: B, position: Int, item: T)

    fun addItem(itemData: T) {
        val position = mDatas.size
        mDatas.add(position, itemData)
        notifyItemInserted(position)
    }

    class BaseViewHolder<B:ViewDataBinding>(var binding: B):RecyclerView.ViewHolder(binding.root)
}