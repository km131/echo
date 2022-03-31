package com.example.echo_kt.ui.main

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
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
import com.example.echo_kt.BaseApplication
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
fun imgPlay(view: View, playStatus: Int) {
    //改变播放键显示
    when (playStatus) {
        PlayerManager.RELEASE, PlayerManager.PAUSE -> {
            view.isSelected = false
            Log.i("iiiii", "暂停")
        }
        PlayerManager.START, PlayerManager.RESUME -> {
            view.isSelected = true
            Log.i("iiiii", "播放")
        }
    }
}

@BindingAdapter("url")
fun setImgUrl(view: ImageView, @DrawableRes url: Int) {
    view.setImageResource(url)
}

@BindingAdapter("urlAlbum")
fun setImgUrl(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load(url ?: R.mipmap.album)
        .apply(RequestOptions.bitmapTransform(CircleCrop()))
        .into(view)
    Log.i("专辑图片", "setImgUrl: $url")
}

@BindingAdapter("urlAlbum2")
fun setImgUrl2(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load(url ?: R.mipmap.album)
        .apply(
            RequestOptions.bitmapTransform(
                MultiTransformation(
                    RoundedCorners(20) //设置图片圆角角度
                )
            )
        )
        .into(view)
    Log.i("专辑图片", "setImgUrl: $url")
}

/**
 * 设置高斯模糊
 * @param radius 1-25 模糊半径
 */
fun getGaussianBlurBitmap(bitmap: Bitmap, radius: Float): Bitmap {
    val inputBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)
    val outputBitmap = Bitmap.createBitmap(inputBitmap)
    val rs = RenderScript.create(BaseApplication.getContext())
    val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
    val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
    blurScript.setRadius(radius)
    blurScript.setInput(tmpIn)
    blurScript.forEach(tmpOut)
    tmpOut.copyTo(outputBitmap)
    return outputBitmap
}

