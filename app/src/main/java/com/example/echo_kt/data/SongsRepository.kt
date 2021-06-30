package com.example.echo_kt.data

import com.example.echo_kt.room.SongDao
import javax.inject.Inject

class SongsRepository  @Inject constructor(private val plantDao: SongDao) {

    fun findSongByIsLike(isLike: Boolean) = plantDao.findSongByIsLike(isLike)
}
