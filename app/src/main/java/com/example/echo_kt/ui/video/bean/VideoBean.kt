package com.example.echo_kt.ui.video.bean

/**
 * map<类型，列表>
 */
class VideoBean(var type:String, var list: ArrayList<VideoMinorBean>){
    class VideoMinorBean(var imgPath: Int, var title:String, var grade:Double){

    }
}