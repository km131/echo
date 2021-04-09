package com.example.echo_kt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.data.AudioBean
import com.example.echo_kt.ui.main.AudioObserver
import com.example.echo_kt.ui.main.MainFragment
import com.example.echo_kt.ui.main.MainViewModel
import java.util.*


/**
 * 作为音频播放观察者,接受到通知立即更新viewModel内状态
 *  间接通过DataBinding更新View
 */
class MainActivity : AppCompatActivity() , AudioObserver {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        PlayerManager.instance.init(this)
        setContentView(R.layout.main_activity)
    }
    override fun onDestroy() {
        super.onDestroy()
        PlayerManager.instance.clear()
        PlayerManager.instance.unregister(this)
    }

    override fun onProgress(currentDuration: Int, totalDuration: Int) {
        viewModel.currentDuration.set(stringForTime(currentDuration))
        viewModel.playProgress.postValue(totalDuration)
    }

    override fun onPlayMode(playMode: Int) {
        when (playMode){
            PlayList.PlayMode.ORDER_PLAY_MODE -> viewModel.playModePic.set(R.mipmap.play_order)
            PlayList.PlayMode.RANDOM_PLAY_MODE ->viewModel.playModePic.set(R.mipmap.play_random)
            PlayList.PlayMode.SINGLE_PLAY_MODE ->viewModel.playModePic.set(R.mipmap.play_single)
        }
    }

    override fun onReset() {
        viewModel.reset()
    }

    override fun onAudioBean(audioBean: AudioBean) {
        viewModel.songName.set(audioBean.name)
        viewModel.singer.set(audioBean.singer)
        viewModel.maxDuration.set(stringForTime(audioBean.duration))
        viewModel.maxProgress.set(audioBean.duration)
        viewModel.albumPic.set(audioBean.albumIdUrl)
    }

    private fun stringForTime(duration: Int): String? {
        val totalSeconds = duration/1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds/60)%60

        return Formatter().format("%02d:%02d",minutes,seconds).toString();
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