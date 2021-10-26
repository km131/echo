package com.example.echo_kt.model

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.echo_kt.api.migu.MiguMusicParameter
import com.example.echo_kt.api.migu.MiguMusicServer
import com.example.echo_kt.api.migu.MiguSearchMusicBean
import com.example.echo_kt.data.SearchBean
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.paging.MiGuPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MiGuMusicModel @Inject constructor(private val service: MiguMusicServer):Model {
    companion object {
        private const val NETWORK_PAGE_SIZE = 25
        fun convertSongBean(bean: MiguSearchMusicBean): SongBean {
            return SongBean(
                songName = bean.data.songItem.songName,
                author = bean.data.songItem.singer,
                albumUrl = bean.data.songItem.albumImgs[1].img,
                audioUrl = bean.data.url,
                id = "miguMusic/${bean.data.songItem.songId}",
                source = "migu"
            ).apply {
                //暂时凑合
                fileType = ".mp3"
            }
        }
    }

    override fun getSearchList(keyWord: String): Flow<PagingData<SearchBean>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { MiGuPagingSource(service, keyWord) }
        ).flow as Flow<PagingData<SearchBean>>
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
}