package com.example.echo_kt.ui.main

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.echo_kt.R
import com.example.echo_kt.play.PlayerManager

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("imgPlay")
fun imgPlay(view: View,playStatus: Int){
    //改变播放键显示
    when(playStatus){
        PlayerManager.RELEASE, PlayerManager.PAUSE ->{
            view.isSelected = false
            Log.i("iiiii", "暂停")
        }
        PlayerManager.START, PlayerManager.RESUME ->{
            view.isSelected = true
            Log.i("iiiii", "播放")
        }
    }
}
@BindingAdapter("url")
fun setImgUrl(view: ImageView,@DrawableRes url: Int) {
    Glide.with(view.context).load(url).into(view)
}

@BindingAdapter("urlAlbum")
fun setImgUrl(view: ImageView, url: String?) {
    url?.apply{
        Glide.with(view.context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(view)
        Log.i("专辑图片", "setImgUrl: $url")
        return
    }
    Glide.with(view.context)
        .load(R.mipmap.album)
        .apply(RequestOptions.bitmapTransform(CircleCrop()))
        .into(view)

    Log.i("专辑图片", "setImgUrl: $url")
}
@BindingAdapter("urlAlbum2")
fun setImgUrl2(view: ImageView, url: String?) {
    url?.apply {
        Glide.with(view.context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(
                MultiTransformation(
                    RoundedCorners(20) //设置图片圆角角度
                )
            ))
            .into(view)
        Log.i("专辑图片", "setImgUrl: $url")
        return
    }
    Glide.with(view.context)
        .load(R.mipmap.album)
        .apply(RequestOptions.bitmapTransform(
            MultiTransformation(
                RoundedCorners(20) //设置图片圆角角度
            )
        ))
        .into(view)
    Log.i("专辑图片", "setImgUrl: $url")
}

