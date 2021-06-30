package com.example.echo_kt.ui.main

import androidx.lifecycle.*
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.data.SongListRepository
import com.example.echo_kt.data.SongsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor(
    songListRepository: SongListRepository,
    songsRepository: SongsRepository
): ViewModel() {
    /**
     * 全部的自定义歌曲列表
     */
    val songList: LiveData<List<SongListBean>> = songListRepository.getAllAudioList().asLiveData()

    /**
     * 选中的歌单索引
     */
    var songlistIndex:Int = 0

    /**
     *  获取将要显示的歌单数据
     *  @param index 该歌单在所有自定义歌单中的索引
     */
    fun readCustomList(index: Int): SongListBean {
        return songList.value!![index]
    }

    val likeSongs: LiveData<List<SongBean>> = songsRepository.findSongByIsLike(true).asLiveData()
}