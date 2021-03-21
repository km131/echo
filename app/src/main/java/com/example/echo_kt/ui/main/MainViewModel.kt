package com.example.echo_kt.ui.main

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo_kt.R
import com.example.echo_kt.play.PlayerManager

class MainViewModel : ViewModel() {
    companion object{
        val instance: MainViewModel by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MainViewModel()
        }
    }
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
    val albumPic = ObservableField<Long>()

    /**
     * 播放状态
     */
    val playStatus = ObservableField<Int>()

    /**
     * 图片播放模式
     */
    val playModePic = ObservableField<Int>().apply {
        set(R.mipmap.play_order)
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

    /**
     * 总长度
     */
    val maxProgress = ObservableField<Int>()

    /**
     * 播放进度
     */
    val playProgress = MutableLiveData<Int>()

//    /**
//     * 是否收藏
//     */
//    val collect = ObservableField<Boolean>()

    /**
     * 重置
     */
    fun reset(){
        songName.set("")
        singer.set("")
        albumPic.set(-1)
        playStatus.set(PlayerManager.RELEASE)
        maxDuration.set("00:00")
        currentDuration.set("00:00")
        maxProgress.set(0)
        playProgress.postValue(0)
//        collect.set(false)
    }

}