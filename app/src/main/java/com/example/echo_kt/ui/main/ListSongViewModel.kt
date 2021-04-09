package com.example.echo_kt.ui.main

import androidx.lifecycle.ViewModel
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.data.AudioBean
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.util.readLocalPlayList

class ListSongViewModel : ViewModel() {

    /**
     * 将要获取的历史或者本地歌曲列表
     */
    var listSongData: MutableList<AudioBean>? = null

    fun scanLocalSong() {
        if (listSongData == null) {
            listSongData = readLocalPlayList(BaseApplication.getContext())
        }
    }

    fun scanHistorySong() {
        if (listSongData == null) listSongData =
            PlayList.instance.historyList.toMutableList().apply {
                reverse()
            }
    }

    private fun toAudioList(list: MutableList<HistoryAudioBean>?): MutableList<AudioBean>? {
        return list?.map {
            collect2Audio(it)
        }?.toMutableList()
    }

    private fun collect2Audio(bean: HistoryAudioBean): AudioBean {
        return AudioBean().apply {
            sortId = bean.sortId
            id = bean.id
            name = bean.name
            singer = bean.singer
            size = bean.size
            duration = bean.duration
            path = bean.path
            albumIdUrl = bean.albumId
        }
    }
}