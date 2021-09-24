package com.example.echo_kt.ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log
import com.example.echo_kt.play.PlayerManager
import kotlin.system.exitProcess

private const val TAG = "MyBroadcastReceiver"
class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "收到广播: ${intent.action}")
        when (intent.action) {
            AudioManager.ACTION_AUDIO_BECOMING_NOISY, "android.intent.action.PHONE_STATE" -> PlayerManager.instance.stopPlay()
            "com.echo_kt.prev" -> PlayerManager.instance.previous()
            "com.echo_kt.pause" -> PlayerManager.instance.controlPlay()
            "com.echo_kt.next" -> PlayerManager.instance.next()
            "com.echo_kt.close" -> {PlayerManager.instance.clear();exitProcess(0)}
        }
    }
}