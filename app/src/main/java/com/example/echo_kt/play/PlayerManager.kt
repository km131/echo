package com.example.echo_kt.play

import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.ui.notification.PlayService
import com.example.echo_kt.ui.setting.SettingViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 *  音频管理
 *  通过单例模式实现,托管音频状态与信息
 *  通过观察者模式统一对状态进行分发
 *  实则是一个代理,将目标对象Player与调用者隔离,并且在内部实现了对观察者的注册与通知
 */
class PlayerManager private constructor() :
    IPlayerStatus {

    /**
     * 单例创建PlayerManager
     */
    companion object {
        val instance: PlayerManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PlayerManager()
        }

        /**
         * 重置
         */
        const val RELEASE = 100

        /**
         * 从头开始播放
         */
        const val START = 200

        /**
         * 播放
         */
        const val RESUME = 300

        /**
         * 暂停
         */
        const val PAUSE = 400
    }

    /**
     * 音乐观察者集合
     */
    private val observers = mutableListOf<AudioObserver>()

    private val playerHelper: IPlayer = MediaPlayerHelper()

    /**
     * 用于关闭rxJava
     */
    private var disposable: Disposable? = null
    private var countdownDisposable: Disposable? = null

    private var settingViewModel: SettingViewModel? = null

    fun getSettingViewModel(): SettingViewModel? {
        return settingViewModel
    }

    /**
     * 播放状态，默认为重置
     */
    private var playStatus = RELEASE

    /**
     * 播放列表
     */
    private lateinit var playList: PlayList

    fun init() {
        playList = PlayList.instance
        playerHelper.setPlayStatus(this)
        startTimer()
    }

    /**
     * 与播放按钮相对应,点击播放按钮存在三种场景
     * 1.播放器还未被初始化,列表为空，点击无用
     * 2.暂停状态: 切换为播放状态,与resume()对应
     * 3.播放状态: 切换为暂停状态,与pause()对应
     */
    fun controlPlay() {
        //对应场景1
        if (playList.currentAudio() == null) {
            playStatus = RELEASE
            //start()
        } else {
            //对应场景3
            if (playerHelper.isPlaying()) {
                pause()
            }
            //对应场景2
            else {
                resume()
            }
        }
    }

    fun stopPlay() {
        if (playerHelper.isPlaying()) {
            pause()
        }
    }

    fun getPlayState(): Int {
        return playStatus
    }

    /**
     * 播放上一首
     */
    fun previous() {
        playNewAudio(playList.previousAudio())
    }

    /**
     * 播放下一首
     */
    fun next() {
        playNewAudio(playList.nextAudio())
    }

    /**
     * 第一次进入,播放器未被初始化,默认播放第一个
     */
    fun startAll() {
        playNewAudio(playList.startAudio())
    }

    /**
     * 播放一个新的音频
     */
    fun playNewAudio(audioBean: SongBean?) {
        if (audioBean == null) {
            //重置
            playerHelper.reset()
            sendResetToObserver()
        } else {
            playStatus = START
            playList.setCurrentAudio(audioBean)
            audioBean.audioUrl.let {
                playerHelper.play(it)
                sendAudioToObserver(audioBean)
                sendPlayStatusToObserver()
            }
        }
        val intent = Intent(BaseApplication.getContext(), PlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            BaseApplication.getContext().startForegroundService(intent)
        } else BaseApplication.getContext().startService(intent)
    }

    /**
     * 从暂停切换为播放
     */
    private fun resume() {
        playStatus = RESUME
        playerHelper.resume()
        sendPlayStatusToObserver()
        val intent = Intent(BaseApplication.getContext(), PlayService::class.java)
        BaseApplication.getContext().startService(intent)
    }

    /**
     * 从播放切换为暂停
     */
    private fun pause() {
        playStatus = PAUSE
        playerHelper.pause()
        sendPlayStatusToObserver()
        val intent = Intent(BaseApplication.getContext(), PlayService::class.java)
        BaseApplication.getContext().startService(intent)
    }

    /**
     * 跳转至指定播放位置
     */
    fun seekTo(duration: Int) {
        playerHelper.seekTo(duration)
    }

    /**
     * 切换播放顺序
     */
    fun switchPlayMode() {
        sendPlayModeToObserver(playList.switchPlayMode())
    }

    /**
     * 获取当前正在播放的音频信息
     */
    fun getCurrentAudioBean(): SongBean? {
        return playList.currentAudio()
    }

    /**
     * 获取当前正在播放的音频大小
     */
    fun getPlayListSize(): Int {
        return playList.getPlayListSize()
    }

    /**
     * 获取播放列表
     */
    fun getPlayList(): MutableList<SongBean> {
        return playList.getPlayList()
    }

    /**
     * 重置并释放播放器
     */
    fun clear() {
        disposable?.dispose()
        playerHelper.reset()
        playerHelper.release()
        playList.clear()
    }

    /**
     * 注册观察者
     */
    fun register(audioObserver: AudioObserver) {
        observers.add(audioObserver)
        notifyObserver(audioObserver)
    }

    /**
     * 解除观察者
     */
    fun unregister(audioObserver: AudioObserver) {
        observers.remove(audioObserver)
    }

    /**
     * 手动更新观察者
     */
    private fun notifyObserver(audioObserver: AudioObserver) {
        playList.currentAudio()?.let { audioObserver.onAudioBean(it) }
        audioObserver.onPlayMode(playList.getCurrentMode())
        audioObserver.onPlayStatus(playStatus)
        //更新播放进度
        //audioObserver.onProgress(playerHelper.getProgress(), it)
    }

    /**
     * 给观察者发送音乐信息
     */
    private fun sendAudioToObserver(audioBean: SongBean) {
        observers.forEach {
            it.onAudioBean(audioBean)
        }
    }

    /**
     * 给观察者发送播放状态
     */
    private fun sendPlayStatusToObserver() {
        observers.forEach {
            it.onPlayStatus(playStatus)
        }
    }

    /**
     * 给观察者发送进度
     */
    private fun sendProgressToObserver(duration: Int) {
        observers.forEach {
//            Log.i(TAG, "sendProgressToObserver: 播放进度：$duration")
            playerHelper.getDuration().let { it1 -> it.onProgress(duration, it1) }
        }
    }

    /**
     * 给观察者发送播放模式
     */
    private fun sendPlayModeToObserver(playMode: Int) {
        observers.forEach {
            it.onPlayMode(playMode)
        }
    }

    /**
     * 给观察者发送播放状态
     */
    private fun sendResetToObserver() {
        observers.forEach {
            it.onReset()
        }
    }

    override fun onBufferingUpdate(percent: Int) {

    }

    override fun onComplete() {
        playList.nextAudio()?.let {
            Log.i("播放结束", "onComplete:下一首：$it ")
            playNewAudio(it)
        }

    }

    /**
     * 开启定时器,用于更新进度
     * 每1000毫秒更新一次
     */
    private fun startTimer() {
        disposable = Observable.interval(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                //仅在播放状态下通知观察者
                if (playerHelper.isPlaying()) {
                    sendProgressToObserver(playerHelper.getProgress())
                }
            }
    }

    fun startTimer(vm: SettingViewModel) {
        settingViewModel = vm
        countdownDisposable = Observable.interval(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                vm.countdownBean.value = (
                        SettingViewModel.CountdownBean(
                            vm.countdownBean.value?.countdown!!.minus(
                                1000
                            ), true
                        )
                        )
                Log.i("TAG", "startTimer:倒计时 " + vm.countdownBean.value + vm)
                //倒计时结束
                if (vm.countdownBean.value!!.countdown <= 0L) {
                    //如果正在播放，则停止播放
                    if (playerHelper.isPlaying()) {
                        controlPlay()
                    }
                    //关闭定时关闭按钮
                    vm.countdownBean.value = (SettingViewModel.CountdownBean(0, false))
                    cleanCountdown()
                }
            }
    }

    //关闭定时结束任务
    fun cleanCountdown() {
        if (playerHelper.isPlaying()) {
            controlPlay()
        }
        countdownDisposable?.dispose()
        settingViewModel = null
    }
}