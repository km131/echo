package com.example.echo_kt.customview

import android.content.Context

/**
 * dp，sp，px 转换类
 *
 * Created by sickworm on 2017/10/16.
 */
object DensityUtils {
    /**
     * dp转换成px
     */
    fun dpConvertToPx(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale
    }

    /**
     * px转换成dp
     */
    fun pxConvertToDp(context: Context, pxValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return pxValue / scale
    }

    /**
     * sp转换成px
     */
    fun spConvertToPx(context: Context, spValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return spValue * fontScale
    }

    /**
     * px转换成sp
     */
    fun pxConvertToSp(context: Context, pxValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return pxValue / fontScale
    }
}