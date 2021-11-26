package com.example.echo_kt.ui.setting

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echo_kt.api.qqmusic.QQMusicServer
import com.example.echo_kt.api.wyymusic.WyyMusicServer
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.ui.main.ListSongViewModel
import com.example.echo_kt.utils.updateUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel@Inject constructor(
     val qqMusicServer: QQMusicServer,
     val wyyMusicServer: WyyMusicServer
) : ViewModel() {
    //定时关闭，倒计时
    val countdownBean = MutableLiveData<CountdownBean>()

    data class CountdownBean(var countdown: Long , var countdownState: Boolean)

    fun updateAudioUrl(callback:()->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            updateUrl(wyyMusicServer, qqMusicServer)
            Log.i("TAG", "onClick: 更新内存中地址")
            PlayList.instance.initHistoryList()
            ListSongViewModel().scanHistorySong()
            withContext(Dispatchers.Main){
                callback.invoke()
            }
        }
    }

}