package com.example.echo_kt.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo_kt.R
import com.example.echo_kt.data.SongListBean

class HomeViewModel : ViewModel() {
    var songListBean :MutableLiveData<List<SongListBean>> = MutableLiveData()
    init {
        songListBean= MutableLiveData(arrayListOf(
            SongListBean("我喜欢",null, R.mipmap.a13),
            SongListBean("收藏",null, R.mipmap.a13)
        ))
    }
}