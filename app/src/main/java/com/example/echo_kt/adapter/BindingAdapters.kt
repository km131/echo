package com.example.echo_kt.adapter

import android.graphics.Bitmap
import android.net.Uri
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
            Log.i("imgPlay", "暂停")
        }
        PlayerManager.START, PlayerManager.RESUME -> {
            view.isSelected = true
            Log.i("imgPlay", "播放")
        }
    }
}

@BindingAdapter("resId")
fun setImgResId(view: ImageView, @DrawableRes resId: Int) {
    view.setImageResource(resId)
}

@BindingAdapter("uriCircle")
fun setImgUriCircle(view: ImageView, path: String?) {
    val transforms = Glide.with(view.context).load(R.mipmap.album).circleCrop()

    Glide.with(view.context)
        .load(if (path != null) Uri.parse(path) else R.mipmap.album)
        .thumbnail(transforms)
        .circleCrop()
        .into(view)
    Log.i("专辑图片", "setImgUriCircle: $path")
}

@BindingAdapter("uriRoundRect")
fun setImgUriRoundRect(view: ImageView, path: String?) {
    val transforms = Glide.with(view.context).load(R.mipmap.album).apply(
        RequestOptions.bitmapTransform(
            MultiTransformation(
                RoundedCorners(20) //设置图片圆角角度
            )
        )
    )
    Glide.with(view.context)
        .load(if (path != null) Uri.parse(path) else R.mipmap.album)
        .thumbnail(transforms)
        .apply(
            RequestOptions.bitmapTransform(
                MultiTransformation(
                    RoundedCorners(20) //设置图片圆角角度
                )
            )
        )
        .into(view)
    Log.i("专辑图片", "setImgUriRoundRect: $path")
}

/**
 * 设置高斯模糊
 * @param radius 1-25 模糊半径
 * @param sampling 越小越清晰,最小应为1
 */
fun getGaussianBlurBitmap(bitmap: Bitmap, radius: Float, sampling: Int): Bitmap {
    val inputBitmap =
        Bitmap.createScaledBitmap(bitmap, bitmap.width / sampling, bitmap.height / sampling, false)
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