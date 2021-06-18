package com.example.echo_kt.ui.search

import androidx.lifecycle.ViewModel
import com.example.echo_kt.data.SearchBean
import com.example.echo_kt.model.KUGOUModel
import com.example.echo_kt.model.MiGuMusicModel
import com.example.echo_kt.model.QQMusicModel
import com.example.echo_kt.model.WyyMusicModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {
    //发送网络请求
    suspend fun sendRequestMessage(keyWord: String, source:String): SearchBean? {
        return withContext(Dispatchers.IO) {
            when(source){
                "KUGOU" -> KUGOUModel().getSearchList(keyWord)
                "QQMUSIC" -> QQMusicModel().getSearchList(keyWord)
                "WYYMUSIC" -> WyyMusicModel().getSearchList(keyWord)
                "MIGUMUSIC" -> MiGuMusicModel().getSearchList(keyWord)
                else -> null
            }
        }
    }
}