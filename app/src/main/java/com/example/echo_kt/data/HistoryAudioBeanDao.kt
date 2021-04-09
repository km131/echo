package com.example.echo_kt.data

import androidx.room.*
import com.example.echo_kt.ui.main.HistoryAudioBean

@Dao
interface HistoryAudioBeanDao {
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
    @Delete
    fun deleteAudio(audioBean: HistoryAudioBean)

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
    @Query("SELECT * FROM history_audio WHERE id=:id")
    fun findAudioById(id: String): HistoryAudioBean?

    /**
     * 查询多个
     */
    @Query("SELECT * FROM history_audio WHERE pathType=:pathType")
    fun findAudioByPathType(pathType: Boolean): MutableList<HistoryAudioBean>?

    /**
     * 返回所有的数据,结果为LiveData
     */
    @Query("SELECT * FROM history_audio")
    fun getAllAudios(): MutableList<HistoryAudioBean>?
}