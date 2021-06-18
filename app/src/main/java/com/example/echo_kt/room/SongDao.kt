package com.example.echo_kt.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.echo_kt.data.SongBean

@Dao
interface SongDao {
    /**
     * 增加一个音频
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSong(song: SongBean)

    /**
     * 增加多条音频
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSongs(songs: MutableList<SongBean>)

    /**
     * 删除一条音频
     */
    @Delete
    fun deleteSong(song: SongBean)

    /**
     * 删除多条音频
     */
    @Delete
    fun deleteSongs(songs: MutableList<SongBean>)

    /**
     * 更新一条音频
     */
    @Update
    fun updateSong(song: SongBean)

    /**
     * 更新多条音频
     */
    @Update
    fun updateSongs(songs: MutableList<SongBean>)


    /**
     * 根据自定义id查询一个
     */
    @Query("SELECT * FROM all_song_list WHERE id = :id")
    fun findSongById(id: String): SongBean?

    /**
     * 根据是否为收藏查询
     */
    @Query("SELECT * FROM all_song_list WHERE isLike = :isLike")
    fun findSongByIsLike(isLike: Boolean): MutableList<SongBean>?
    /**
     * 根据来源查询
     */
    @Query("SELECT * FROM all_song_list WHERE source = :source")
    fun findSongBySource(source: String): MutableList<SongBean>?

    /**
     * 返回所有的数据
     */
    @Query("SELECT * FROM all_song_list")
    fun getAllSongs(): MutableList<SongBean>?
}