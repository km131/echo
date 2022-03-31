package com.example.echo_kt.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.example.echo_kt.BaseApplication.Companion.getContext
import com.example.echo_kt.R
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.play.PlayerManager
import androidx.media.app.NotificationCompat as MediaNotificationCompat

/**
 * 每播放一个新的音频或者播放状态改变，发送一个新的通知
 */
class PlayServiceNew : MediaBrowserServiceCompat() {
    companion object {
        private const val MY_MEDIA_ROOT_ID = "media_root_id"
        private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"
    }
    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(this, "PlayService").apply {

            // 启用MediaButtons和TransportControls的回调
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                    or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )

            // 使用ACTION_PLAY设置初始的PlaybackState，这样媒体按钮就可以启动播放器
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())

            // MySessionCallback()有一些方法可以处理来自媒体控制器的回调
            setCallback(MySessionCallback())
            setSessionToken(sessionToken)
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        // (Optional) Control the level of access for the specified package name.
        // You'll need to write your own logic to do this.
        return if (allowBrowsing(clientPackageName, clientUid)) {
            // Returns a root ID that clients can use with onLoadChildren() to retrieve
            // the content hierarchy.
            MediaBrowserServiceCompat.BrowserRoot(MY_MEDIA_ROOT_ID, null)
        } else {
            // Clients can connect, but this BrowserRoot is an empty hearcihy
            // so onLoadChildren returns nothing. This disables the ability to browse for content.
            MediaBrowserServiceCompat.BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null)
        }
    }

    private fun allowBrowsing(clientPackageName: String, clientUid: Int): Boolean {
        return clientPackageName == packageName
    }

    override fun onLoadChildren(
        parentMediaId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        //  Browsing not allowed
        if (MY_EMPTY_MEDIA_ROOT_ID == parentMediaId) {
            result.sendResult(null)
            return
        }

        // Assume for example that the music catalog is already loaded/cached.

        val mediaItems = emptyList<MediaBrowserCompat.MediaItem>()

        // Check if this is the root menu:
        if (MY_MEDIA_ROOT_ID == parentMediaId) {
            // Build the MediaItem objects for the top level,
            // and put them in the mediaItems list...
        } else {
            // Examine the passed parentMediaId to see which submenu we're at,
            // and put the children of that menu in the mediaItems list...
        }
        result.sendResult(mediaItems.toMutableList())
    }


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
        val builder = NotificationCompat.Builder(this.baseContext, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.mipmap.echo)
            .addAction(
                R.drawable.ic_prev, "上一首", PendingIntent.getBroadcast(
                    applicationContext,
                    0,
                    Intent("com.echo_kt.prev").setPackage(
                        packageName
                    ),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            ) // #0
            .addAction(NotificationCompat.Action(
                R.drawable.ic_stop,
                "暂停",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    applicationContext,
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            ))
//            .addAction(
//                R.mipmap.play_pause_gray, "播放", PendingIntent.getBroadcast(
//                    applicationContext,
//                    0,
//                    Intent("com.echo_kt.pause").setPackage(
//                        packageName
//                    ),
//                    PendingIntent.FLAG_UPDATE_CURRENT
//                )
//            ) // #1
            .addAction(
                R.drawable.ic_next, "下一首", PendingIntent.getBroadcast(
                    applicationContext,
                    0,
                    Intent("com.echo_kt.next").setPackage(
                        packageName
                    ),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            ) // #2
            .setStyle(
                MediaNotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0,1,2)
                    .setMediaSession(mediaSession?.sessionToken)
            )
            .setContentTitle(results?.songName)
            .setContentText(results?.author)
            .setLargeIcon(results?.getAlbumBitmap())
        startForeground(1024, builder.build())
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

    override fun onDestroy() {
        mediaSession?.run {
            isActive = false
            release()
        }
    }
}

class MySessionCallback : MediaSessionCompat.Callback() {
    override fun onPlay() {
        super.onPlay()
    }
}