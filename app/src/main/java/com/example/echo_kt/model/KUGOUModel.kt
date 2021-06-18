package com.example.echo_kt.model

import com.example.echo_kt.api.kugou.KuGouServer
import com.example.echo_kt.api.kugou.SearchMusicDetails
import com.example.echo_kt.api.kugou.KuGouSearchBean
import com.example.echo_kt.api.kugou.Info
import com.example.echo_kt.data.SongBean

class KUGOUModel{
    suspend fun getSearchList(keyWord: String): KuGouSearchBean?{
        val format = "json"
        val pageSize = 30
        val page = 1
        return try {
            KuGouServer.create().searchMusicList(format, keyWord, page,pageSize,showtype =  1)
        }catch (e:Exception){
            null
        }
    }

    suspend fun getMusicBean(albumId:String,hash:String): SearchMusicDetails.Data? {
        //参数aid请求时可能会返回空值，但请求数据时不能为空，故改为0
        var aid = "0"
        if (albumId != "")
            aid = albumId
        return try {
            KuGouServer.create2().searchMusic(
                aid = aid,
                hash = hash
            ).data
        } catch (e: Exception) {
            null
        }
    }
    fun convertSongBean(bean: Info,albumUrl:String,audioUrl:String): SongBean {
        return SongBean(
            songName = bean.songName,
            author = bean.singerName,
            albumUrl = albumUrl,
            audioUrl = audioUrl,
            id = "kugouMusic/${bean.albumAudioId}",
            source = "kugou"
        ).apply {
            requestParameter = hashMapOf("id" to bean.albumId , "hash" to bean.hash)
        }
    }
}