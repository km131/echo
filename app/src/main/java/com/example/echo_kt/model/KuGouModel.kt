package com.example.echo_kt.model

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.echo_kt.api.kugou.KuGouServer
import com.example.echo_kt.api.kugou.SearchMusicDetails
import com.example.echo_kt.api.kugou.Info
import com.example.echo_kt.data.SearchBean
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.paging.KuGouPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KuGouModel @Inject constructor(private val service: KuGouServer):Model{
    companion object {
        private const val NETWORK_PAGE_SIZE = 25

        fun convertSongBean(bean: Info,albumUrl:String,audioUrl:String,lyric:String): SongBean {
            return SongBean(
                songName = bean.songName,
                author = bean.singerName,
                albumUrl = albumUrl,
                audioUrl = audioUrl,
                id = "kugouMusic/${bean.albumAudioId}",
                source = "kugou",
            ).apply {
                requestParameter = hashMapOf("id" to bean.albumId , "hash" to bean.hash)
                fileType = bean.format
                this.lyric = lyric
            }
        }
        suspend fun getMusicBean(albumId:String,hash:String): SearchMusicDetails.Data? {
            //参数aid请求时可能会返回空值，但请求数据时不能为空，故改为0
            var aid = "0"
            if (albumId != "")
                aid = albumId
            return try {
                KuGouServer.create2().searchMusic(
                    albumId = aid,
                    hash = hash
                ).data
            } catch (e: Exception) {
                null
            }
        }
    }
    override fun getSearchList(keyWord: String): Flow<PagingData<SearchBean>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { KuGouPagingSource(service, keyWord) }
        ).flow as Flow<PagingData<SearchBean>>
    }
}