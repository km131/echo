package com.example.echo_kt.room

import androidx.room.*
import com.example.echo_kt.ui.main.HistoryAudioBean

@Dao
interface HistorySongListDao {
    /**
     * 增加一条音频
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAudio(audioBean: HistoryAudioBean)

    /**
     * 增加多条音频,除了List之外，也可以使用数组
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAudios(audioBean: MutableList<HistoryAudioBean>)

    /**
     * 删除一条音频
     */
    @Query("DELETE FROM history_audio WHERE songId = :audioBeanId")
    fun deleteAudio(audioBeanId: String)

    /**
     * 删除多条音频
     */
    @Delete
    fun deleteAudios(audioBean: MutableList<HistoryAudioBean>)

    /**
     * 更新一条音频
     */
    @Update
    fun updateAudio(audioBean: HistoryAudioBean)

    /**
     * 更新多条音频
     */
    @Update
    fun updateAudios(audioBean: MutableList<HistoryAudioBean>)

    /**
     * 查询一个
     */
    @Query("SELECT * FROM history_audio WHERE songId=:id")
    fun findAudioById(id: String): HistoryAudioBean?

    /**
     * 查询该歌单的所有歌曲
     */
    @Transaction
    @Query("SELECT * FROM history_audio")
    fun getAllSongs(): List<HistoryListAndSongs>?
}