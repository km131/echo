package com.example.echo_kt.model

import com.example.echo_kt.api.qqmusic.*
import com.example.echo_kt.data.SongBean
import okhttp3.ResponseBody
import retrofit2.Call
import kotlin.math.sign


class QQMusicModel {
    private var qqMusicServer1: QQMusicServer  = QQMusicServer.create()
    private var qqMusicServerVKey: QQMusicServer  = QQMusicServer.createVKey()
    private var qqMusicServer2: QQMusicServer = QQMusicServer.create2()
    private var qqMusicParameter: QQMusicParameter = QQMusicParameter()

    suspend fun getSearchList(keyword: String): ListSearchResponse? {
        return try {
            qqMusicServer1.searchList(
                n = "30",
                w = keyword,
                loginUin = "3452341293",
                format = "json"
            )
        }catch (e:Exception){
            null
        }
    }
    suspend fun getVKey(mid:String): GetVKeyResponse? {
        val data = qqMusicParameter.getData(mid)
        return try {
            qqMusicServerVKey.searchVKey(sign = qqMusicParameter.getSign(data),loginUin = "3452341293",data = data)
        }catch (e:Exception){
            null
        }
    }
    suspend fun getPath(mid:String): String {
        val vk = getVKey(mid)
        return vk?.let {
             "https://ws.stream.qqmusic.qq.com/${vk.req.midurlinfo.reqData[0].purl}"
        }?: ""
    }
    fun getAudioFile(url:String): Call<ResponseBody> {
        return qqMusicServer2.getAudioFile(url = url)
    }
    fun convertSongBean(audioList: AudioList, url: String, parameterMap:HashMap<String,String>):SongBean{
        return SongBean(
            songName = audioList.songName,
            author = audioList.singer[0].singerName,
            albumUrl = "https://y.gtimg.cn/music/photo_new/T002R300x300M000${audioList.album}.jpg",
            audioUrl = url,
            id = "qqMusic/${audioList.songmid}",
            source = "qq"
        ).apply {
            requestParameter = parameterMap
        }
    }
}