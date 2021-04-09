package com.example.echo_kt

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.data.AudioBean

/**
 *
 */
class PlayService : Service() {

    private val CHANNEL_ID="echo_1024"

    override fun onCreate() {
        super.onCreate()
        PlayerManager.instance.init(BaseApplication.getContext())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val results = PlayerManager.instance.getCurrentAudioBean()
        createNotification(results)
        return START_NOT_STICKY
    }

    private fun createNotification(results: AudioBean?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        val expandedView = RemoteViews(
            applicationContext.packageName, R.layout.notify_player_big
        )

        setListeners(expandedView)

        val noti = NotificationCompat.Builder(this.baseContext, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.mipmap.a12)
            .setContentTitle("ECHO")
            .setContent(expandedView)
            .setOngoing(true)
            .build()
        with(NotificationManagerCompat.from(this)) {
            notify(1024,noti)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        PlayerManager.instance.clear()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name ="echo"
            val descriptionText = "播放通知"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    fun setListeners(view: RemoteViews) {
        try {
            var pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                Intent("com.echo_kt.prev").setPackage(
                    packageName
                ),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            view.setOnClickPendingIntent(R.id.player_previous, pendingIntent)
//            pendingIntent = PendingIntent.getBroadcast(
//                applicationContext,
//                0,
//                Intent("com.echo_kt.prev").setPackage(
//                    packageName
//                ),
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//            view.setOnClickPendingIntent(R.id.player_close, pendingIntent)
            pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                Intent("com.echo_kt.pause").setPackage(
                    packageName
                ),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            view.setOnClickPendingIntent(R.id.player_play, pendingIntent)
            pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                Intent("com.echo_kt.next").setPackage(
                    packageName
                ),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            view.setOnClickPendingIntent(R.id.player_next, pendingIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}