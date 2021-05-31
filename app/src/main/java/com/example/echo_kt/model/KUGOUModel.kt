package com.example.echo_kt.model

import android.util.Log
import com.example.echo_kt.api.KuGouServer
import com.example.echo_kt.api.SearchMusicDetails
import com.example.echo_kt.data.CustomSearchBean
import com.example.echo_kt.data.Info
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KUGOUModel{
    suspend fun getSearchList(keyWord: String): CustomSearchBean?{
        val format = "json"
        val pageSize = 20
        val page = 1
        return try {
            KuGouServer.create().searchMusicList(format, keyWord, page,pageSize,showtype =  1)
        }catch (e:Exception){
            null
        }
    }

    suspend fun getMusicBean(item: Info): SearchMusicDetails.Data? {
        Log.i("albumId=", ": ${item.albumId}")
        //参数aid请求时可能会返回空值，但请求数据时不能为空，故改为0
        var aid = "0"
        if (item.albumId != "")
            aid = item.albumId
        return try {
            KuGouServer.create2().searchMusic(
                aid = aid,
                hash = item.hash
            ).data
        } catch (e: Exception) {
            null
        }
    }
}