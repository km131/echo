package com.example.echo_kt.ui.main

import androidx.lifecycle.ViewModel
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.util.readLocalPlayList

class ListSongViewModel : ViewModel() {

    /**
     * 将要获取的历史或者本地歌曲列表
     */
    var listSongData: MutableList<SongBean>? = null

    fun scanLocalSong() {
        listSongData = readLocalPlayList(BaseApplication.getContext())
    }

    fun scanHistorySong() {
        listSongData = PlayList.instance.getHistoryList().toMutableList().apply {
            reverse()
        }
    }
}