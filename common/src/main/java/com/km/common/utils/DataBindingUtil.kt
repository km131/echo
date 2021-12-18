package com.km.common.utils

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions


@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

/**
 * @param radius 圆角角度（设置为圆形后失效）
 * @param isCircleCrop 是否将图片设置为圆形
 * @param placeDrawableId 占位图
 * @param errorDrawableId 错误图
 */
@BindingAdapter("imgUrl", "radius", "isCircleCrop", "placeDrawableId", "errorDrawableId", requireAll = false)
fun setImgUrl(
    view: ImageView,
    imgUrl: String,
    radius: Int=0,
    isCircleCrop: Boolean = false,
    @DrawableRes errorDrawableId: Int?,
    @DrawableRes placeDrawableId: Int?
) {
    Glide.with(view.context)
        .load(imgUrl)
        .apply(
            RequestOptions.bitmapTransform(
                if (isCircleCrop) {
                    CircleCrop()
                } else {
                    MultiTransformation(RoundedCorners(radius))
                }
            ),
        ).let {
            placeDrawableId?.let { it1 -> it.placeholder(it1) }
            errorDrawableId?.let { it1 -> it.error(it1) }
            it
        }
        .into(view)
}
