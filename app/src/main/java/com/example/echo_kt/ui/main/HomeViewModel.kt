package com.example.echo_kt.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.R
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.util.readCustomPlayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    /**
     * 全部的自定义歌曲列表
     */
    var songList: MutableLiveData<List<SongListBean>> = MutableLiveData()

    fun readCustomList(index:Int): SongListBean {
        //此循环的目的时等待value的值在io流中读取到，写法错误有待改进
        while (songList.value==null){
            if (songList.value!=null)
                break
        }
        return songList.value!![index]
    }
    init {
        GlobalScope.launch(Dispatchers.IO) {
            songList = MutableLiveData(readCustomPlayList())
        }
    }
}