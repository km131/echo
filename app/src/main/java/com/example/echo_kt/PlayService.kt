package com.example.echo_kt

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.echo_kt.ui.main.PlayerManager

/**
 * 空壳，只做播放初始化
 */
class PlayService : Service() {

    override fun onCreate() {
        super.onCreate()
        PlayerManager.instance.init(BaseApplication.getContext())
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        PlayerManager.instance.clear()
    }
}