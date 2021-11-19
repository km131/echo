package com.example.echo_kt.play

import android.media.MediaPlayer
import android.media.MediaPlayer.*
import android.util.Log
import android.widget.Toast
import com.example.echo_kt.api.showToast

/**
 * des 基于MediaPlayer实现的音频播放
 * @author zs
 * @data 2020/6/25
 */
class MediaPlayerHelper : IPlayer,
    OnCompletionListener,
    OnBufferingUpdateListener,
    OnErrorListener,
    OnPreparedListener{

    private val mediaPlayer by lazy {
        MediaPlayer()
    }
    private var iPlayStatus: IPlayerStatus? = null

    init {
        //播放完成监听
        mediaPlayer.setOnCompletionListener(this)
        //缓冲更新监听
        mediaPlayer.setOnBufferingUpdateListener(this)
        //错误监听
        mediaPlayer.setOnErrorListener { _, _, _ ->
            showToast("播放地址已过时，请手动更新,自动切换下一首")
            onCompletion(mediaPlayer)
            true
        }
        //播放器准备完成监听
        mediaPlayer.setOnPreparedListener(this)
    }

    override fun setPlayStatus(iPlayStatus: IPlayerStatus) {
        this.iPlayStatus = iPlayStatus
    }

    override fun play(path: String) {
        mediaPlayer.reset()
        //可能会抛FileNotFound异常
        kotlin.runCatching {
            Log.i("MediaPlayHelper", "播放地址: $path")
            mediaPlayer.setDataSource(path)
        }.onSuccess {
            //异步加载网络音频避免卡顿导致界面挂起
            mediaPlayer.prepareAsync()
        }.onFailure {
            showToast("播放异常 path = $path")
            Log.i("MediaPlayHelper", "播放异常: $path")
        }
    }

    override fun resume() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun stop() {
        mediaPlayer.stop()
    }

    override fun seekTo(duration: Int) {
        mediaPlayer.seekTo(duration)
    }

    override fun reset() {
        mediaPlayer.reset()
    }

    override fun release() {
        mediaPlayer.release()
    }

    /**
     * 获取是否正在播放
     */
    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    /**
     * 获取当前播放进度
     */
    override fun getProgress(): Int {
        return mediaPlayer.currentPosition
    }
    /**
     * 获取歌曲时长
     */
    override fun getDuration(): Int {
        return mediaPlayer.duration
    }


    /**
     * 播放完成
     */
    override fun onCompletion(mp: MediaPlayer?) {
        Log.i("MediaPlayHelper", "onCompletion: 播放完成")
        iPlayStatus?.onComplete()
    }

    /**
     * 缓冲更新
     */
    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        iPlayStatus?.onBufferingUpdate(percent)
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return true
    }

    /**
     * mediaPlayer准备完毕直接播放
     */
    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer.start()
    }

}