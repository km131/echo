package com.example.echo_kt.data

import com.example.echo_kt.ui.main.AudioBean

data class SongListBean(val name:String,val list: MutableList<AudioBean>?,val coverImage:Int) {
}