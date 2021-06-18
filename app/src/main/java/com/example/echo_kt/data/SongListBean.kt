package com.example.echo_kt.data

import androidx.room.*
import com.example.echo_kt.room.Converters

@Entity(tableName = "custom_audio_list")
@TypeConverters(Converters::class)
class SongListBean(
    /**
     * 歌单id
     */
    @ColumnInfo(name = "id") var id: String,
    /**
     * 歌单名
     */
    @ColumnInfo(name = "name") var name: String,
    /**
     * 创建日期
     */
    @ColumnInfo(name = "date") var date: String,

    /**
     * 歌单封面地址
     */
    @ColumnInfo(name = "coverImage") var coverImage: String
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playlistId")
    var playlistId: Long=0
    /**
     * 歌单内歌曲数量
     */
    @ColumnInfo(name = "number")
    var number: Long = 0
}