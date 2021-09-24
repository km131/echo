package com.example.echo_kt.model

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.echo_kt.api.qqmusic.*
import com.example.echo_kt.data.SearchBean
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.paging.QQPagingSource
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject


class QQMusicModel @Inject constructor(private val listService: QQMusicServer) :Model {
    private val qqMusicServerVKey: QQMusicServer  = QQMusicServer.createVKey()
    private val qqMusicServer2: QQMusicServer = QQMusicServer.create2()
    private val qqMusicParameter: QQMusicParameter = QQMusicParameter()
    companion object {
        private const val NETWORK_PAGE_SIZE = 25
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
    override fun getSearchList(keyWord: String): Flow<PagingData<SearchBean>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { QQPagingSource(listService, keyWord) }
        ).flow as Flow<PagingData<SearchBean>>
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
}