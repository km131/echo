package com.example.echo_kt.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.echo_kt.api.wyymusic.WyyMusicServer
import com.example.echo_kt.api.wyymusic.WyySearchListBean
import com.example.echo_kt.util.getWyyParameter
import com.google.gson.Gson

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class WyyPagingSource(
    private val service: WyyMusicServer,
    private val keyword: String
) : PagingSource<Int, WyySearchListBean.Result.Song>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WyySearchListBean.Result.Song> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        val key =
            "{\"hlpretag\":\"<span class=\\\"s-fc7\\\">\",\"hlposttag\":\"</span>\",\"s\":\"$keyword\",\"type\":\"1\",\"offset\":\"$page\",\"total\":\"true\",\"limit\":\"20\",\"csrf_token\":\"\"}"
        val params = getWyyParameter(key)
        return try {
            Log.i("WYYPagingSource", "params: "+params.params+",  encSecKey:" +params.encSecKey)
            val response = service.searchList(params.params, params.encSecKey)
            Log.d("WYYPagingSource", "获取网易云音乐的数据列表:\n ${Gson().toJson(response)}")
            val info = response.result.songs
            LoadResult.Page(
                data = info,
                prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (info.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, WyySearchListBean.Result.Song>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}