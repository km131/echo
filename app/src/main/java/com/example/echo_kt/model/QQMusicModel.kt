package com.example.echo_kt.model

import com.example.echo_kt.api.qqmusic.*
import com.example.echo_kt.data.AudioBean
import okhttp3.ResponseBody
import retrofit2.Call


class QQMusicModel {
    private var qqMusicServer1: QQMusicServer  = QQMusicServer.create()
    private var qqMusicServerVKey: QQMusicServer  = QQMusicServer.createVKey()
    private var qqMusicServer2: QQMusicServer = QQMusicServer.create2()
    private var qqMusicParameter: QQMusicParameter = QQMusicParameter()

    suspend fun getSearchList(keyword: String): ListSearchResponse? {
        return try {
            qqMusicServer1.searchList(
                n = "10",
                w = keyword,
                loginUin = "2602241712",
                format = "json"
            )
        }catch (e:Exception){
            null
        }
    }
    suspend fun getVKey(mid:String): GetVKeyResponse? {
        val data = qqMusicParameter.getData(mid)
        return try {
            qqMusicServerVKey.searchVKey(sign = qqMusicParameter.getSign(data),loginUin = "2602241712",data = data)
        }catch (e:Exception){
            null
        }
    }
    fun getAudioFile(url:String): Call<ResponseBody> {
        return qqMusicServer2.getAudioFile(url = url)
    }
    fun convertAudioBean(audioList: AudioList,url: String):AudioBean{
        return AudioBean().apply {
            name = audioList.songName
            singer = audioList.singer[0].singerName
            albumIdUrl = "https://y.gtimg.cn/music/photo_new/T002R300x300M000${audioList.album}.jpg"
            path = url
            id = "qqMusic/${audioList.mediaMid}"
            pathType = true
        }
    }
}