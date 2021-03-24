package com.example.echo_kt.ui.douban.bean

/**
 * map<类型，列表>
 */
class DoubanBean(var type:String,var list: ArrayList<DoubanMinorBean>){
    class DoubanMinorBean(var imgPath: Int,var title:String,var grade:Double){

    }
}