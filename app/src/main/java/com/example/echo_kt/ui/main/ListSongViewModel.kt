package com.example.echo_kt.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.util.readLocalPlayList

class ListSongViewModel : ViewModel() {
    /**
     * 将要获取的历史或者本地歌曲列表
     */
    val listSongData: MutableLiveData<MutableList<SongBean>?> by lazy {
        MutableLiveData<MutableList<SongBean>?>()
    }

    fun scanLocalSong() {
        listSongData.postValue(readLocalPlayList(BaseApplication.getContext()))
    }

    fun scanHistorySong() {
        listSongData.postValue(PlayList.instance.getHistoryList().toMutableList().apply {
            reverse()
        })
    }
}