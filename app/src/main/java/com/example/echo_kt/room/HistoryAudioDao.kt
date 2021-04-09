package com.example.echo_kt.room

import androidx.room.*
import com.example.echo_kt.data.AudioBean

/**
 * des 用于音频数据库增删改查
 * @author zs
 * @date 2020/10/29
 */
@Dao
interface HistoryAudioDao {

    /**
     * 增加一条音频
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAudio(audioBean: AudioBean)

    /**
     * 增加多条音频,除了List之外，也可以使用数组
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAudios(audioBean: MutableList<AudioBean>)

    /**
     * 删除一条音频
     */
    @Delete
    fun deleteAudio(audioBean: AudioBean)

    /**
     * 删除多条音频
     */
    @Delete
    fun deleteAudios(audioBean: MutableList<AudioBean>)

    /**
     * 更新一条音频
     */
    @Update
    fun updateAudio(audioBean: AudioBean)

    /**
     * 更新多条音频
     */
    @Update
    fun updateAudios(audioBean: MutableList<AudioBean>)


    /**
     * 查询一个
     */
    @Query("SELECT * FROM history_audio WHERE id=:id")
    fun findAudioById(id: Long): AudioBean?

    /**
     * 返回所有的数据,结果为LiveData
     */
    @Query("SELECT * FROM history_audio")
    fun getAllAudios(): MutableList<AudioBean>?

}