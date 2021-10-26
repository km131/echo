package com.example.echo_kt.model

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.echo_kt.api.wyymusic.WyyMusicServer
import com.example.echo_kt.api.wyymusic.WyyPathBean
import com.example.echo_kt.api.wyymusic.WyySearchListBean
import com.example.echo_kt.data.SearchBean
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.paging.WyyPagingSource
import com.example.echo_kt.util.getWyyParameter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class WyyMusicModel @Inject constructor(private val service: WyyMusicServer) : Model {

    companion object {
        private const val NETWORK_PAGE_SIZE = 25
        fun convertSongBean(
            pathBean: WyyPathBean.Data,
            listBean: WyySearchListBean.Result.Song
        ): SongBean {
            var authorStr = ""
            for (i in listBean.author.indices) {
                authorStr += listBean.author[i].name + "/"
            }
            authorStr = authorStr.substring(0, authorStr.length - 1)
            return SongBean(
                songName = listBean.name,
                author = authorStr,
                albumUrl = listBean.album.picUrl,
                audioUrl = pathBean.url,
                id = "WyyMusic/${listBean.mid}",
                source = "wyy"
            ).apply {
                val parameterMap = HashMap<String, String>()
                parameterMap["musicId"] = listBean.mid
                requestParameter = parameterMap
                fileType = pathBean.type
            }
        }
    }

    override fun getSearchList(keyWord: String): Flow<PagingData<SearchBean>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { WyyPagingSource(service, keyWord) }
        ).flow as Flow<PagingData<SearchBean>>
    }

    suspend fun getSongPath(musicId: String): WyyPathBean? {
        val key =
            "{\"ids\":\"[$musicId]\",\"level\":\"standard\",\"encodeType\":\"aac\",\"csrf_token\":\"\"}"
        val params = getWyyParameter(key)
        return try {
            service.searchPath(params.params, params.encSecKey)
        } catch (e: Exception) {
            null
        }
    }
}