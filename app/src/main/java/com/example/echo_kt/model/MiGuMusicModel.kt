package com.example.echo_kt.model

import android.util.Log
import com.example.echo_kt.api.migu.MiguMusicParameter
import com.example.echo_kt.api.migu.MiguMusicServer
import com.example.echo_kt.api.migu.MiguSearchListBean
import com.example.echo_kt.api.migu.MiguSearchMusicBean
import com.example.echo_kt.data.SongBean

class MiGuMusicModel {
    suspend fun getSearchList(keyWord: String):MiguSearchListBean?{
        val feature = "1011000000"
        val pageSize = "30"
        val searchSwitch =
            "{\"song\":1,\"album\":0,\"singer\":0,\"tagSong\":1,\"mvSong\":0,\"bestShow\":1,\"songlist\":0,\"lyricSong\":0}"
        val r = MiguMusicParameter().getSearchListHeaders(keyWord)
        Log.i("咪咕", "onCreateView: $r")
        return try {
            MiguMusicServer.create(0).searchList(
                r, feature, pageSize,
                keyWord, searchSwitch
            )
        }catch (e:Exception){
            null
        }
    }

    /**
     * @param toneFlag 音质
     */
    suspend fun getMusicBean(albumId:String,songId:String,toneFlag:String):MiguSearchMusicBean?{
        val r = MiguMusicParameter().getSearchMusicHeaders()
        Log.i("咪咕", "onCreateView: $r")
        return try {
            MiguMusicServer.create(1).searchMusicBean(
                r, albumId,songId,
                toneFlag,resourceType =  "2",lowerQualityContentId = "600900000001234567"
            )
        }catch (e:Exception){
            null
        }
    }
    fun convertSongBean(bean: MiguSearchMusicBean): SongBean {
        return SongBean(
            songName = bean.data.songItem.songName,
            author = bean.data.songItem.singer,
            albumUrl = bean.data.songItem.albumImgs[1].img,
            audioUrl = bean.data.url,
            id = "miguMusic/${bean.data.songItem.songId}",
            source = "migu"
        )
    }
}