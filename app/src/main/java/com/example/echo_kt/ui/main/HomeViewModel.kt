package com.example.echo_kt.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.util.readCustomPlayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    /**
     * 全部的自定义歌曲列表
     */
    val songList: MutableLiveData<List<SongListBean>> by lazy {
        MutableLiveData<List<SongListBean>>()
    }

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

    init {
        GlobalScope.launch(Dispatchers.IO) {
            songList.postValue(readCustomPlayList())
        }
    }
}