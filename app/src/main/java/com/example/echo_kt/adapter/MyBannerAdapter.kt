package com.example.echo_kt.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.data.BannerBean
import com.youth.banner.adapter.BannerAdapter

class MyBannerAdapter(val list: List<BannerBean>) : BannerAdapter<BannerBean, MyBannerAdapter.BannerViewHolder>(list) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val imageView: ImageView = ImageView(parent?.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder,
        data: BannerBean,
        position: Int,
        size: Int
    ) {
        holder.imageView.setImageResource(data.imagePath)
    }

    class BannerViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
}