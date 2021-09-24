package com.example.echo_kt.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.echo_kt.api.kugou.KuGouServer
import com.example.echo_kt.api.migu.MiguMusicServer
import com.example.echo_kt.api.qqmusic.QQMusicServer
import com.example.echo_kt.api.wyymusic.WyyMusicServer
import com.example.echo_kt.data.SearchBean
import com.example.echo_kt.model.*
import com.example.echo_kt.ui.SourceType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
   val kuGouServer: KuGouServer,
   val qqMusicServer: QQMusicServer,
   val wyyMusicServer: WyyMusicServer,
   val miGuMusicServer: MiguMusicServer
) : ViewModel() {
    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<SearchBean>>? = null
    private val kuGouModel = KuGouModel(kuGouServer)
    private val qqMusicModel = QQMusicModel(qqMusicServer)
    private val wyyMusicModel = WyyMusicModel(wyyMusicServer)
    private val miGuMusicModel = MiGuMusicModel(miGuMusicServer)

    fun sendRequestMessage(keyWord: String, source: SourceType): Flow<PagingData<SearchBean>> {
        currentQueryValue = keyWord
        val model: Model = when (source) {
            SourceType.KUGOU -> kuGouModel
            SourceType.QQMUSIC -> qqMusicModel
            SourceType.WYYMUSIC -> wyyMusicModel
            SourceType.MIGUMUSIC -> miGuMusicModel
        }
        val newResult: Flow<PagingData<SearchBean>> =
            model.getSearchList(keyWord).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}