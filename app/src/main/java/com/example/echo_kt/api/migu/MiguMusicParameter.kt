package com.example.echo_kt.api.migu

class MiguMusicParameter {

    fun getSearchListHeaders(keyword:String):Map<String,String>{
        return Params().getMap(keyword)
    }
    fun getSearchMusicHeaders():Map<String,String>{
        return mapOf(
            //channel 为任意数字(7位任意数字只能获取非vip)(0146951可以获取vip音乐)
            "channel" to "0146951",
            "Host" to "app.c.nf.migu.cn"
        )
    }
}