package com.example.echo_kt.model

import androidx.paging.PagingData
import com.example.echo_kt.data.SearchBean
import kotlinx.coroutines.flow.Flow

interface Model {
    fun getSearchList(keyWord:String): Flow<PagingData<SearchBean>>
}
