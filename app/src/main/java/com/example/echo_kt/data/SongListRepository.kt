package com.example.echo_kt.data

import com.example.echo_kt.room.CustomSongListDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongListRepository@Inject constructor(private val songListDao: CustomSongListDao) {

    fun getAllAudioList() = songListDao.getAllAudioLists()

    //fun getPlaylistsWithSongs(songListId: Long) = plantDao.getPlaylistsWithSongs(songListId)

}
