package com.example.echo_kt.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.echo_kt.api.migu.MiguMusicParameter
import com.example.echo_kt.api.migu.MiguMusicServer
import com.example.echo_kt.api.migu.MiguSearchListBean
import com.google.gson.Gson

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class MiGuPagingSource(
    private val service: MiguMusicServer,
    private val keyWord: String
) : PagingSource<Int, MiguSearchListBean.SongResultData.ResultXX>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MiguSearchListBean.SongResultData.ResultXX> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            val feature = "1011000000"
            val pageSize = "30"
            val searchSwitch =
                "{\"song\":1,\"album\":0,\"singer\":0,\"tagSong\":1,\"mvSong\":0,\"bestShow\":1,\"songlist\":0,\"lyricSong\":0}"
            val r = MiguMusicParameter().getSearchListHeaders(keyWord)
            val response = service.searchList(
                r, feature, pageSize,
                keyWord, searchSwitch
            )
            Log.d("MiGuPagingSource", "获取m咪咕音乐的数据列表:\n ${Gson().toJson(response)}")
            val info = response.songResultData
            LoadResult.Page(
                data = info.result,
                prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page==3) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MiguSearchListBean.SongResultData.ResultXX>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}