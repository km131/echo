package com.example.echo_kt.ui.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.echo_kt.data.AudioBean

@Entity(tableName = "history_audio")
class HistoryAudioBean {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sortId")
    var sortId: Long = 0

    /**
     * 歌曲id
     */
    @ColumnInfo(name = "id")
     var id: String = ""

    /**
     * 歌曲名
     */
    @ColumnInfo(name = "name")
     var name: String? = null

    /**
     * 歌手
     */
    @ColumnInfo(name = "singer")
     var singer: String? = null

    /**
     * 歌曲所占空间大小
     */
    @ColumnInfo(name = "size")
     var size: Long = 0

    /**
     * 歌曲时间长度
     */
    @ColumnInfo(name = "duration")
     var duration = 0

    /**
     * 歌曲地址
     */
    @ColumnInfo(name = "path")
     var path: String? = null

    /**
     * 播放地址是否为网络链接
     */
    @ColumnInfo(name = "pathType")
    var pathType: Boolean = false
    /**
     * 若播放地址为网络链接，下面两个为请求地址的参数
     */
    @ColumnInfo(name = "kugouAid")
    var kugouAid: String = ""
    @ColumnInfo(name = "kugouHash")
    var kugouHash:String = ""

    /**
     * 图片地址
     */
    @ColumnInfo(name = "albumUrl")
     var albumId: String = ""

    companion object{
        /**
         * 将AudioBean转换为HistoryAudioBean
         */
        fun audio2History(bean: AudioBean): HistoryAudioBean {
            return HistoryAudioBean().apply {
                sortId = bean.sortId
                id = bean.id
                name = bean.name
                singer = bean.singer
                size = bean.size
                duration = bean.duration
                path = bean.path
                albumId = bean.albumIdUrl
                pathType = bean.pathType
                kugouAid = bean.kugouAid
                kugouHash = bean.kugouHash
            }
        }

        fun historyList2AudioList(list: MutableList<HistoryAudioBean>): MutableList<AudioBean> {
            return list.map {
                AudioBean().apply {
                    sortId = it.sortId
                    id = it.id
                    name = it.name
                    singer = it.singer
                    size = it.size
                    duration = it.duration
                    path = it.path
                    albumIdUrl = it.albumId
                    pathType = it.pathType
                    kugouAid = it.kugouAid
                    kugouHash = it.kugouHash
                }
            }.toMutableList()
        }
    }
}
