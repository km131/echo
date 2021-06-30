package com.example.echo_kt.room

import androidx.room.*
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.ui.main.HistoryAudioBean

data class PlaylistWithSongs(
    @Embedded val playlist: SongListBean,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "id",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val songs: List<SongBean>
)
@Entity(primaryKeys = ["playlistId", "id"])
data class PlaylistSongCrossRef(
    val playlistId: Long,
    //@ColumnInfo(index = true)
    val id: String
)

data class HistoryListAndSongs(
    @Embedded val history: HistoryAudioBean,
    @Relation(
        parentColumn = "songId",
        entityColumn = "id"
    )
    val playlists: SongBean
)
