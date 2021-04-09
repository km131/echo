package com.example.echo_kt.room

import androidx.room.*
import com.example.echo_kt.data.SongListBean

@Dao
interface CustomSongListDao {
    /**
     * 增加一个歌单
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSongList(audioBean: SongListBean)

    /**
     * 删除一个歌单
     */
    @Delete
    fun deleteSongList(audioBean: SongListBean)

    /**
     * 删除多个歌单
     */
    @Delete
    fun deleteSongLists(audioBean: MutableList<SongListBean>)

    /**
     * 更新一个歌单
     */
    @Update
    fun updateSongList(audioBean: SongListBean)

    /**
     * 查询一个
     */
    @Query("SELECT * FROM custom_audio_list WHERE id=:id")
    fun findAudioById(id: String): SongListBean?

    /**
     * 返回所有的数据,结果为LiveData
     */
    @Query("SELECT * FROM custom_audio_list")
    fun getAllAudioLists(): MutableList<SongListBean>?
}