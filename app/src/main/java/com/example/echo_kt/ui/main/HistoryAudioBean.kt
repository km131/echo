package com.example.echo_kt.ui.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_audio")
class HistoryAudioBean : AudioBean() {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sortId")
    override var sortId: Long = 0

    /**
     * 歌曲id
     */
    @ColumnInfo(name = "id")
    override var id: Long = 0

    /**
     * 歌曲名
     */
    @ColumnInfo(name = "name")
    override var name: String? = null

    /**
     * 歌手
     */
    @ColumnInfo(name = "singer")
    override var singer: String? = null

    /**
     * 歌曲所占空间大小
     */
    @ColumnInfo(name = "size")
    override var size: Long = 0

    /**
     * 歌曲时间长度
     */
    @ColumnInfo(name = "duration")
    override var duration = 0

    /**
     * 歌曲地址
     */
    @ColumnInfo(name = "path")
    override var path: String? = null

    /**
     * 图片id
     */
    @ColumnInfo(name = "albumId")
    override var albumId: Long = 0
}
