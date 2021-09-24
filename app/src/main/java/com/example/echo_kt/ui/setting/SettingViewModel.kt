package com.example.echo_kt.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo_kt.api.qqmusic.QQMusicServer
import com.example.echo_kt.api.wyymusic.WyyMusicServer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel@Inject constructor(
     val qqMusicServer: QQMusicServer,
     val wyyMusicServer: WyyMusicServer
) : ViewModel() {
    //定时关闭，倒计时
    val countdownBean = MutableLiveData<CountdownBean>()

    data class CountdownBean(var countdown: Long , var countdownState: Boolean)

}