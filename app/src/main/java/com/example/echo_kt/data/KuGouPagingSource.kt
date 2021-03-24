package com.example.echo_kt.data

import android.util.Log
import androidx.paging.PagingSource
import com.example.echo_kt.api.KuGouServer
import com.example.echo_kt.api.MusicBean
import com.example.echo_kt.api.SearchBean

private const val KUGOU_STARTING_PAGE_INDEX = 1

//class KuGouPagingSource(
//    private val service: KuGouServer,
//    private val keyWord: String
//) :PagingSource<Int,SearchBean>(){
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchBean> {
//        val page = params.key ?: KUGOU_STARTING_PAGE_INDEX
//        return try {
//            val response = service.searchMusicList(
//                format = "json",
//                keyWord = keyWord, page = page,paceSize = 20,showtype = 1
//                , perpage = params.loadSize
//            )
//            val music = response
//            Log.i("列表数据", "load: $music")
//            LoadResult.Page(
//                data = listOf(music),
//                prevKey = if (page == KUGOU_STARTING_PAGE_INDEX) null else page - 1,
//                nextKey = if (page == response.totalPages) null else page + 1
//            )
//        } catch (exception: Exception) {
//            LoadResult.Error(exception)
//        }
//    }
//}