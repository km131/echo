package com.example.echo_kt.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingViewModel : ViewModel() {
    //定时关闭，倒计时
    val countdownBean = MutableLiveData<CountdownBean>()

    data class CountdownBean(var countdown: Long , var countdownState: Boolean)
}