package com.example.echo_kt.util

import android.content.Context
import android.util.Log
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.data.AudioBean

/**
 * 获取音频列表数据
 */
fun initAudioData(context: Context): MutableList<AudioBean>? {
    return if (PlayerManager.instance.getPlayList().size == 0) {
        Log.i("", "initAudioData: 播放列表长度为0")
        PlayerManager.instance.getPlayList()
    } else {
        Log.i("", "initAudioData: 播放列表长度为${PlayerManager.instance.getPlayList().size}")
        PlayerManager.instance.getPlayList()
    }
}