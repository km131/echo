package com.example.echo_kt.ui.main

open class AudioBean {
    /**
     * 歌曲名
     */
    open var name: String? = null

    /**
     * 歌手
     */
    open var singer: String? = null

    /**
     * 歌曲所占空间大小
     */
    open var size: Long = 0

    /**
     * 歌曲时间长度
     */
    open var duration = 0

    /**
     * 歌曲地址
     */
    open var path: String? = null

    /**
     * 图片id
     */
    open var albumId: Long = 0

    /**
     * 歌曲id
     */
    open var id: Long = 0

    /**
     * 排序id
     */
    open var sortId: Long = 0

    override fun toString(): String {
        return "\nAudioBean(sortId=$sortId,name=$name, singer=$singer, size=$size, duration=$duration, path=$path, albumId=$albumId, id=$id)"
    }

    fun copy(bean: AudioBean):AudioBean{
        return AudioBean().apply {
            sortId = bean.sortId
            id = bean.id
            name = bean.name
            singer = bean.singer
            size = bean.size
            duration = bean.duration
            path = bean.path
            albumId = bean.albumId
        }
    }

}
