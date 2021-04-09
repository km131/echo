package com.example.echo_kt.ui.main

import com.example.echo_kt.data.AudioBean

interface AudioObserver {
    /**
     * 歌曲信息
     * 空实现,部分界面可不用实现
     */
    fun onAudioBean(audioBean: AudioBean){}

    /**
     * 播放状态,目前有四种。可根据类型进行扩展
     * release
     * start
     * resume
     * pause
     *
     * 空实现,部分界面可不用实现
     */
    fun onPlayStatus(playStatus:Int){}

    /**
     * 当前播放进度
     * 空实现,部分界面可不用实现
     */
    fun onProgress(currentDuration: Int,totalDuration:Int){}

    /**
     * 播放模式
     */
    fun onPlayMode(playMode:Int)

    /**
     * 重置
     */
    fun onReset()
}
