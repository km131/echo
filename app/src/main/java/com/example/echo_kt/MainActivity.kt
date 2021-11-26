package com.example.echo_kt

import android.content.IntentFilter
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.ui.main.MainFragment
import com.example.echo_kt.ui.main.MainViewModel
import com.example.echo_kt.ui.notification.MyBroadcastReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


/**
 * 作为音频播放观察者,接受到通知立即更新viewModel内状态
 *  间接通过DataBinding更新View
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    private val viewModel: MainViewModel by viewModels()
    private val myBroadcastReceiver = MyBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("MainActivity1", "onCreate:${System.currentTimeMillis()}" )
//        installSplashScreen()
        //updateUrl()
        PlayerManager.instance.init()
        setContentView(R.layout.main_activity)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        registerReceiver(myBroadcastReceiver, IntentFilter().apply {
            //音频输出切回到内置扬声器广播
            addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
            //来电广播
            addAction("android.intent.action.PHONE_STATE")
            addAction("com.echo_kt.prev")
            addAction("com.echo_kt.pause")
            addAction("com.echo_kt.next")
            addAction("com.echo_kt.close")
        })
        Log.e("MainActivity2", "onCreate:${System.currentTimeMillis()}" )
    }

    override fun onResume() {
        super.onResume()
        //设置按音量键调节音乐音量，此调用会将音量控件连接到 STREAM_MUSIC，确保音量控件能够调节正确音频流的音量
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myBroadcastReceiver)
    }
    //    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            moveTaskToBack(false)
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }
    override fun onBackPressed() {
        //获取hostFragment
        val mMainNavFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        //获取当前所在的fragment
        val fragment =
            mMainNavFragment?.childFragmentManager?.primaryNavigationFragment
        //如果当前处于根fragment即HostFragment
        if (fragment is MainFragment) {
            //Activity退出但不销毁
            moveTaskToBack(false)
        } else {
            super.onBackPressed()
        }
    }
}