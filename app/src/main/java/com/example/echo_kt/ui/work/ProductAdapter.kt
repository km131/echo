package com.example.echo_kt.ui.work

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.echo_kt.data.ProductBean
import com.example.echo_kt.databinding.ListItemFragmentProductBinding

class ProductAdapter(
    private val values: List<ProductBean>
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemFragmentProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values[position])
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(private val binding : ListItemFragmentProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:ProductBean){
            binding.apply {
                binding.data=item
                binding.textOrImage=(item.text!=null)
                executePendingBindings()
            }
        }
    }
}