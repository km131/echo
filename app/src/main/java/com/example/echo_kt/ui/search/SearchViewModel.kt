package com.example.echo_kt.ui.search

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.echo_kt.api.KuGouServer
import com.example.echo_kt.api.SearchBean
import com.example.echo_kt.data.CustomSearchBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {
    suspend fun sendRequestMessage(keyWord: String): CustomSearchBean {
        return withContext(Dispatchers.IO) {
            KuGouServer.create().searchMusicList("json", keyWord, 1, 20, 1)
        }
    }
}