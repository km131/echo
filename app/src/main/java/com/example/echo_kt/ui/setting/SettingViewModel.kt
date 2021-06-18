package com.example.echo_kt.ui.setting

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo_kt.api.kugou.KuGouServer
import com.example.echo_kt.model.QQMusicModel
import com.example.echo_kt.model.WyyMusicModel
import com.example.echo_kt.room.AppDataBase
import com.example.echo_kt.util.readHistoryPlayList
import com.example.echo_kt.util.updateUrl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {
    //定时关闭，倒计时
    val countdownBean = MutableLiveData<CountdownBean>()

    data class CountdownBean(var countdown: Long , var countdownState: Boolean)

}