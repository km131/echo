package com.example.echo_kt.room

import androidx.room.*
import com.example.echo_kt.data.SongListBean
import kotlinx.coroutines.flow.Flow

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
    @Query("DELETE FROM custom_audio_list WHERE id = :listId")
    fun deleteSongList(listId: String)

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
     * 查询该歌单的基本信息
     */
    @Transaction
    @Query("SELECT * FROM custom_audio_list WHERE playlistId=:playlistId")
    fun getPlaylistInfo(playlistId:Long): SongListBean

    /**
     * 查询该歌单的所有歌曲
     */
    @Transaction
    @Query("SELECT * FROM custom_audio_list WHERE playlistId=:playlistId")
    fun getPlaylistsWithSongs(playlistId:Long): Flow<PlaylistWithSongs>

    /**
     * 向歌单增加歌曲
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistsWithSong(playlistSongCrossRef: PlaylistSongCrossRef)
    /**
     * 删除歌曲
     */
    @Transaction
    @Delete
    fun deletePlaylistsWithSong(playlistSongCrossRef: PlaylistSongCrossRef)

    /**
     * 向歌单增加多个歌曲
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistsWithSongs(playlistSongCrossRef: List<PlaylistSongCrossRef>)

    /**
     * 查询一个
     */
    @Query("SELECT * FROM custom_audio_list WHERE id=:id")
    fun findAudioById(id: String): SongListBean?

    /**
     * 返回所有的数据
     */
    @Query("SELECT * FROM custom_audio_list")
    fun getAllAudioLists(): Flow<List<SongListBean>>
}