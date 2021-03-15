package com.example.echo_kt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.ui.main.AudioBean
import com.example.echo_kt.ui.main.AudioObserver
import com.example.echo_kt.ui.main.MainFragment
import com.example.echo_kt.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
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
        viewModel.playProgress.set(totalDuration)
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
        super.onAudioBean(audioBean)
        viewModel.songName.set(audioBean.name)
        viewModel.singer.set(audioBean.singer)
        viewModel.maxDuration.set(stringForTime(audioBean.duration))
        viewModel.maxProgress.set(audioBean.duration)
        viewModel.albumPic.set(audioBean.albumId)
    }

    private fun stringForTime(duration: Int): String? {
        val totalSeconds = duration/1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds/60)%60

        return Formatter().format("%02d:%02d",minutes,seconds).toString();
    }
}