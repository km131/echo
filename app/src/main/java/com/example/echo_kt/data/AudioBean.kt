package com.example.echo_kt.data

class AudioBean {
    /**
     * 歌曲名
     */
     var name: String? = null

    /**
     * 歌手
     */
     var singer: String? = null

    /**
     * 歌曲所占空间大小
     */
     var size: Long = 0

    /**
     * 歌曲时间长度
     */
     var duration = 0

    /**
     * 歌曲地址
     */
     var path: String? = null

    /**
     * 图片地址
     */
    var albumIdUrl:String= ""

    /**
     * 歌曲id
     */
     var id: String = ""

    /**
     * 排序id
     */
     var sortId: Long = 0

    /**
     * 歌曲地址是否为网络链接
     */
    var pathType: Boolean = false

    /**
     * 若播放地址为网络链接，下面两个为请求地址的参数
     */
    var kugouAid: String = ""
    var kugouHash:String = ""

    constructor(
        name: String?,
        singer: String?,
        size: Long,
        duration: Int,
        path: String?,
        albumId: String,
        songId:String,
        pathType:Boolean,
        kugouAid:String,
        kugouHash:String
    ) {
        this.name = name
        this.singer = singer
        this.size = size
        this.duration = duration
        this.path = path
        this.albumIdUrl = albumId
        this.id=songId
        this.pathType=pathType
        this.kugouAid=kugouAid
        this.kugouHash=kugouHash
    }

    constructor()

    override fun toString(): String {
        return "\nAudioBean(sortId=$sortId,name=$name, singer=$singer, size=$size, duration=$duration, path=$path, id=$id)"
    }

}
