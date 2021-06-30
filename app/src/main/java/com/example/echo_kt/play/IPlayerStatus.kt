package com.example.echo_kt.play

interface IPlayerStatus {
    /**
     * 缓冲更新
     */
    fun onBufferingUpdate(percent: Int)

    /**
     * 播放结束
     */
    fun onComplete()

}
