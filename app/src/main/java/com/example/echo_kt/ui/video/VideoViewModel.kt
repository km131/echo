package com.example.echo_kt.ui.video

import androidx.lifecycle.ViewModel
import com.example.echo_kt.R
import com.example.echo_kt.ui.video.bean.VideoBean
import com.example.echo_kt.ui.video.bean.VideoBean.VideoMinorBean

class VideoViewModel : ViewModel() {
    fun getDataList(): ArrayList<VideoBean> {
        val s = R.mipmap.echo
        return arrayListOf(
            VideoBean("电影", arrayListOf(
                VideoMinorBean(s,"电影1",32.1),
                VideoMinorBean(s,"电影1",32.1),
                VideoMinorBean(s,"电影1",32.1),
                VideoMinorBean(s,"电影2",32.1),
                VideoMinorBean(s,"电影1",32.1))
            ),
            VideoBean("电视剧", arrayListOf(
                VideoMinorBean(s,"电视剧1",32.1),
                VideoMinorBean(s,"电视剧1",32.1),
                VideoMinorBean(s,"电视剧1",32.1),
                VideoMinorBean(s,"电视剧1",32.1),
                VideoMinorBean(s,"电视剧2",32.1))
            ),
            VideoBean("纪录片", arrayListOf(
                VideoMinorBean(s,"纪录片1",32.1),
                VideoMinorBean(s,"纪录片1",32.1),
                VideoMinorBean(s,"纪录片1",32.1),
                VideoMinorBean(s,"纪录片1",32.1),
                VideoMinorBean(s,"纪录片2",32.1))
            ),
            VideoBean("纪录片", arrayListOf(
                VideoMinorBean(s,"纪录片1",32.1),
                VideoMinorBean(s,"纪录片1",32.1),
                VideoMinorBean(s,"纪录片1",32.1),
                VideoMinorBean(s,"纪录片1",32.1),
                VideoMinorBean(s,"纪录片2",32.1))
            )
        )
    }
}