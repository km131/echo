package com.example.echo_kt.ui.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.NotificationTarget
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.R
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.play.PlayerManager

/**
 * 每播放一个新的音频或者播放状态改变，发送一个新的通知
 */
const val CHANNEL_ID = "echo_1024"

class PlayService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val results = PlayerManager.instance.getCurrentAudioBean()
        val mode = PlayerManager.instance.getPlayState()
        createNotification(results, mode)
        return START_NOT_STICKY
    }

    private fun createNotification(results: SongBean?, mode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        val expandedView = RemoteViews(
            applicationContext.packageName,
            R.layout.notify_player_big
        )

        setListeners(expandedView)
        val noti: Notification = NotificationCompat.Builder(this.baseContext, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.mipmap.echo)
            .setContentTitle("ECHO")
            .setContent(expandedView)
            .setOngoing(true)
            .build()
        when (mode) {
            PlayerManager.RESUME, PlayerManager.START -> noti.contentView.setImageViewResource(
                R.id.player_play,
                R.drawable.ic_stop
            )
            PlayerManager.PAUSE -> noti.contentView.setImageViewResource(
                R.id.player_play,
                R.drawable.ic_play
            )
        }

        results?.getAlbumBitmap()?.let {
            noti.contentView.setImageViewBitmap(
                R.id.player_album_art,
                it
            )
            noti.contentView.setTextViewText(R.id.player_song_name, results.songName)
            noti.contentView.setTextViewText(R.id.player_author_name, results.author)
        } ?: noti.contentView.setImageViewResource(
            R.id.player_album_art,
            R.mipmap.album
        )
        with(NotificationManagerCompat.from(this)) {
            notify(1024, noti)
            startForeground(1024, noti)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "echo"
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

    private fun setListeners(view: RemoteViews) {
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
            //暂停和播放共用一个事件
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
            pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                Intent("com.echo_kt.close").setPackage(
                    packageName
                ),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            view.setOnClickPendingIntent(R.id.player_close, pendingIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}