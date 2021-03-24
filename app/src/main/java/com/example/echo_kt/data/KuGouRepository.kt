package com.example.echo_kt.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.echo_kt.api.KuGouServer
import com.example.echo_kt.api.MusicBean
import com.example.echo_kt.api.SearchBean
import kotlinx.coroutines.flow.Flow

//class KuGouRepository constructor(private val service: KuGouServer) {
//
//    fun getSearchResultStream(query: String): Flow<PagingData<SearchBean>> {
//        return Pager(
//            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
//            pagingSourceFactory = { KuGouPagingSource(service, query) }
//        ).flow
//    }
//
//    companion object {
//        private const val NETWORK_PAGE_SIZE = 25
//    }
//}
