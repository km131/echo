package com.example.echo_kt.ui.main

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.R
import com.example.echo_kt.customview.FloatWindowView
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.ui.float_window.ItemViewTouchListener
import java.lang.ref.WeakReference

class MainViewModel : ViewModel() {
    /**
     * 歌名
     */
    val songName = ObservableField<String>().apply { set("暂无播放") }

    /**
     * 歌手
     */
    val singer = ObservableField<String>().apply { set("") }

    /**
     * 专辑图片
     */
    val albumPic = ObservableField<String>()

    /**
     * 播放状态
     */
    val playStatus = ObservableField<Int>()

    /**
     * 图片播放模式
     */
    val playModePic = ObservableField<Int>().apply {
        set(R.drawable.ic_order_play)
    }

    /**
     * 文字播放模式
     */
    val playModeText = ObservableField<String>().apply {
        set("顺序播放")
    }

    /**
     * 总播放时长-文本
     */
    val maxDuration = ObservableField<String>().apply {
        set("00:00")
    }

    /**
     * 当前播放时长-文本
     */
    val currentDuration = ObservableField<String>().apply {
        set("00:00")
    }
    val lyricStr = ObservableField<String>().apply {
        set("暂无歌词")
    }

    /**
     * 总长度
     */
    val maxProgress = ObservableField<Int>()

    /**
     * 播放进度
     */
    val playProgress = MutableLiveData<Int>()

    /**
     * 悬浮窗状态
     */
    val floatWindowViewState = MutableLiveData(false)

//    /**
//     * 是否收藏
//     */
//    val collect = ObservableField<Boolean>()
    private var windowManager: WindowManager? = null
    private var floatWindowView: WeakReference<FloatWindowView?> = WeakReference(null)

    /**
     * 重置
     */
    fun reset() {
        songName.set("")
        singer.set("")
        albumPic.set("")
        playStatus.set(PlayerManager.RELEASE)
        maxDuration.set("00:00")
        currentDuration.set("00:00")
        maxProgress.set(0)
        playProgress.postValue(0)
        lyricStr.set("暂无歌词")
//        collect.set(false)
    }

    fun showFloatWindow() {
        //如果悬浮窗已存在，取消创建。此情况在打开悬浮窗后重新进入设置页面时会出现
        if (floatWindowViewState.value!!) {
            return
        }
        windowManager =
            BaseApplication.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParam = getLayoutParams()
        floatWindowView.get()?.run {
            windowManager!!.addView(this, layoutParam)
        } ?: run {
            floatWindowView = WeakReference(FloatWindowView(BaseApplication.getContext()).apply {
                setOnTouchListener(ItemViewTouchListener(layoutParam, windowManager!!))
            })
            windowManager!!.addView(floatWindowView.get(), layoutParam)
        }
        floatWindowViewState.value = true
    }

    fun closeFloatWindow() {
        floatWindowView.let { it1 ->
            windowManager?.removeView(it1.get())
            floatWindowView.clear()
            Log.e("TAG", "closeFloatWindow")
        }
        floatWindowViewState.value = false
    }

    private fun getLayoutParams(): WindowManager.LayoutParams {
        val layoutParams = WindowManager.LayoutParams()
        return layoutParams.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            flags = (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    )
            format = PixelFormat.TRANSLUCENT
            width = 100
            height = 100
            layoutAnimationParameters
            gravity = Gravity.CENTER
        }
    }

}