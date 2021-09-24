package com.example.echo_kt.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.echo_kt.api.kugou.Info
import com.example.echo_kt.api.kugou.KuGouServer
import com.google.gson.Gson

private const val UNSPLASH_STARTING_PAGE_INDEX = 1
class KuGouPagingSource(
    private val service: KuGouServer,
    private val keyWord: String
): PagingSource<Int, Info>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Info> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            val response = service.searchMusicList("json", keyWord, page,30,showtype =  1)
            Log.d("KuGouPagingSource", "获取酷狗音乐的数据列表:\n ${Gson().toJson(response)}")
            val info = response.data.info
            LoadResult.Page(
                data = info,
                prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page==3) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Info>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}