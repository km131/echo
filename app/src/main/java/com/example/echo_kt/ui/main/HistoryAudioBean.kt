package com.example.echo_kt.ui.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_audio")
class HistoryAudioBean {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sortId")
    var sortId: Long = 0

    /**
     * 歌曲id
     */
    @ColumnInfo(name = "id")
     var id: Long = 0

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
     * 图片id
     */
    @ColumnInfo(name = "albumId")
     var albumId: Long = 0
}
