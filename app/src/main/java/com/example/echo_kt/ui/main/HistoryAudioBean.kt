package com.example.echo_kt.ui.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * sortId 该歌曲在该列表的序号
 * id 用来在all_song_list中查询该歌曲
 */
@Entity(tableName = "history_audio")
data class HistoryAudioBean(
    /**
     * 歌曲id
     */
    @ColumnInfo(name = "songId")
    var songId: String
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sortId")
    var sortId: Long = 0
}
