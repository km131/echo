package com.example.echo_kt.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.echo_kt.api.qqmusic.AudioList
import com.example.echo_kt.api.qqmusic.QQMusicServer
import com.google.gson.Gson

private const val UNSPLASH_STARTING_PAGE_INDEX = 1
class QQPagingSource(
    private val service: QQMusicServer,
    private val keyword: String
): PagingSource<Int, AudioList>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AudioList> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            val response = service.searchList( n = "20",
                w = keyword,
                loginUin = "123456789",
                format = "json",page=page)
            Log.d("QQPagingSource", "获取QQ音乐的数据列表:\n ${Gson().toJson(response)}")
            val info = response.data.songList.data
            LoadResult.Page(
                data = info,
                prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (info.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AudioList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}