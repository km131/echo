package com.example.echo_kt.ui.douban

import androidx.lifecycle.ViewModel
import com.example.echo_kt.R
import com.example.echo_kt.ui.douban.bean.DoubanBean
import com.example.echo_kt.ui.douban.bean.DoubanBean.DoubanMinorBean

class DoubanViewModel : ViewModel() {
    fun getDataList(): ArrayList<DoubanBean> {
        return arrayListOf(
            DoubanBean("电影", arrayListOf(
                DoubanMinorBean(R.mipmap.h1,"电影1",32.1),
                DoubanMinorBean(R.mipmap.h1,"电影1",32.1),
                DoubanMinorBean(R.mipmap.h1,"电影1",32.1),
                DoubanMinorBean(R.mipmap.h1,"电影2",32.1),
                DoubanMinorBean(R.mipmap.h1,"电影1",32.1))
            ),
            DoubanBean("电视剧", arrayListOf(
                DoubanMinorBean(R.mipmap.h1,"电视剧1",32.1),
                DoubanMinorBean(R.mipmap.h1,"电视剧1",32.1),
                DoubanMinorBean(R.mipmap.h1,"电视剧1",32.1),
                DoubanMinorBean(R.mipmap.h1,"电视剧1",32.1),
                DoubanMinorBean(R.mipmap.h1,"电视剧2",32.1))
            ),
            DoubanBean("纪录片", arrayListOf(
                DoubanMinorBean(R.mipmap.h1,"纪录片1",32.1),
                DoubanMinorBean(R.mipmap.h1,"纪录片1",32.1),
                DoubanMinorBean(R.mipmap.h1,"纪录片1",32.1),
                DoubanMinorBean(R.mipmap.h1,"纪录片1",32.1),
                DoubanMinorBean(R.mipmap.h1,"纪录片2",32.1))
            ),
            DoubanBean("纪录片", arrayListOf(
                DoubanMinorBean(R.mipmap.h1,"纪录片1",32.1),
                DoubanMinorBean(R.mipmap.h1,"纪录片1",32.1),
                DoubanMinorBean(R.mipmap.h1,"纪录片1",32.1),
                DoubanMinorBean(R.mipmap.h1,"纪录片1",32.1),
                DoubanMinorBean(R.mipmap.h1,"纪录片2",32.1))
            )
        )
    }
}