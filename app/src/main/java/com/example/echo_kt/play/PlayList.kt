package com.example.echo_kt.play

import android.util.Log
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.api.KuGouServer
import com.example.echo_kt.room.AppDataBase
import com.example.echo_kt.data.AudioBean
import com.example.echo_kt.ui.main.HistoryAudioBean
import com.example.echo_kt.util.readHistoryPlayList
import com.example.echo_kt.util.readLocalPlayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * des 播放列表
 * 当历史和收藏列表需要改变时,数据库和内存中列表(手动更新)同时更新,UI与内存列表保持一致
 * 这样做的意图是避免每次操作历史/收藏列表时频繁读取数据库做数据同步
 */
class PlayList private constructor() {

    /**
     * 单例创建PlayerManager
     */
    companion object {
        val instance: PlayList by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PlayList()
        }
    }

    /**
     * 当前播放列表
     */
    private var currentAudioList = mutableListOf<AudioBean>()

    /**
     * 本地播放列表,本地资源
     */
    private var _localList = mutableListOf<AudioBean>()

    /**
     * 只读，下同
     */
    private var localList: MutableList<AudioBean> = _localList

    fun getLocalList():MutableList<AudioBean>{
        return localList
    }
    /**
     * 默认播放列表,历史
     */
    private var _historyList = mutableListOf<AudioBean>()
    private var historyList: List<AudioBean> = _historyList
    fun getHistoryList():List<AudioBean>{
        return historyList
    }

    /**
     * 播放模式，默认为顺序播放
     */
    private var playMode = PlayMode.ORDER_PLAY_MODE


    init {
        //读取播放列表
        GlobalScope.launch(Dispatchers.IO) {
            _localList = readLocalPlayList(BaseApplication.getContext())
            localList = _localList
            _historyList = readHistoryPlayList()
            historyList = _historyList
        }
        initPlayList()
    }

    /**
     *  初始化播放列表,将历史列表作为播放列表
     */
    private fun initPlayList() {
        if (currentAudioList.size == 0){
            currentAudioList.addAll(historyList)
        }
    }
    /**
     * 当前正在播放的音频 默认为null
     */
    private var currentAudio: AudioBean? = null

    /**
     * 当前播放音频对象在对应播放列表的索引
     */
    private var currentIndex = 0

    /**
     * 当前正在播放的音频
     */
    fun currentAudio(): AudioBean? {
        return currentAudio
    }

    /**
     * 设置当前播放列表和currentIndex
     */
    fun setCurrentAudio(audioBean: AudioBean) {
        addRecord(audioBean)
        initPlayList()
        //重置当前角标
        currentIndex = getIndexByAudio(audioBean)
    }

    /**
     * 第一次进入播放的音频，默认为播放列表的第一个
     */
    fun startAudio(): AudioBean? {
        currentAudio = currentAudioList[0]
        return currentAudio
    }

    /**
     * 替换播放列表
     */
    fun switchAudioList(list:MutableList<AudioBean>){
        currentAudioList.apply {
            clear()
            addAll(list)
        }
    }

    /**
     * 下一个音频
     */
    fun nextAudio(): AudioBean? {
        if (currentAudioList.size>0) {
            when (playMode) {
                //顺序播放
                PlayMode.ORDER_PLAY_MODE -> {
                    currentIndex = if (currentIndex < currentAudioList.size - 1) {
                        currentIndex + 1
                    } else {
                        0
                    }
                }
                //单曲循环(不做处理)
                PlayMode.SINGLE_PLAY_MODE -> {
                }
                //随机播放
                PlayMode.RANDOM_PLAY_MODE -> {
                    currentIndex = getRandom(0, currentAudioList.size - 1)
                }
            }
            currentAudio = currentAudioList[currentIndex]
        } else {
            //当前播放列表为空将当前播放置为null
            currentAudio = null
        }
        return currentAudio
    }

    /**
     * 上一个音频
     */
    fun previousAudio(): AudioBean? {
        if (currentAudioList.size>0) {
            when (playMode) {
                //顺序
                PlayMode.ORDER_PLAY_MODE -> {
                    currentIndex = if (currentIndex > 1) {
                        currentIndex - 1
                    } else {
                        currentAudioList.size - 1
                    }
                }
                //单曲(不做处理)
                PlayMode.SINGLE_PLAY_MODE -> {
                }
                //随机
                PlayMode.RANDOM_PLAY_MODE -> {
                    currentIndex = getRandom(0, currentAudioList.size - 1)
                }
            }
            currentAudio = currentAudioList[currentIndex]
        } else {
            //当前播放列表为空将当前播放置为null
            currentAudio = null
        }
        return currentAudio
    }

    /**
     * 切换播放模式
     * 顺序播放点击 会切换至单曲
     * 单曲播放点击 会切换至随机
     * 随机播放点击 会切换至顺序
     */
    fun switchPlayMode(): Int {
        when (playMode) {
            PlayMode.ORDER_PLAY_MODE -> {
                playMode =
                    PlayMode.SINGLE_PLAY_MODE
            }
            PlayMode.SINGLE_PLAY_MODE -> {
                playMode =
                    PlayMode.RANDOM_PLAY_MODE
            }
            PlayMode.RANDOM_PLAY_MODE -> {
                playMode =
                    PlayMode.ORDER_PLAY_MODE
            }
        }
        return playMode
    }

    /**
     * 获取当前播放模式
     */
    fun getCurrentMode(): Int {
        return playMode
    }

    /**
     * 重置，将当前播放重置为null
     */
    fun clear() {
        currentAudio = null
    }

    /**
     * 通过currentAudio获取所在的index
     */
    private fun getIndexByAudio(audioBean: AudioBean): Int {
        //设置当前正在播放的对象
        currentAudio = audioBean
        for (index in 0 until currentAudioList.size) {
            if (audioBean == currentAudioList[index]) {
                return index
            }
        }
        //默认返回0
        return 0
    }
    fun getIndex():Int{
        return currentIndex
    }

    /**
     * 获取播放列表长度
     */
    fun getPlayListSize(): Int {
        return currentAudioList.size
    }

    /**
     * 设置下一首播放歌曲
     */
    fun setNextPlay(audioBean: AudioBean) {
        if (currentAudioList.size>0){
            currentAudioList.add(currentIndex+1,audioBean)
        }
    }

    /**
     * 获取播放列表
     */
    fun getPlayList(): MutableList<AudioBean> {
        return currentAudioList
    }

    class PlayMode {
        companion object {
            /**
             * 顺序播放-默认
             */
            const val ORDER_PLAY_MODE = 9

            /**
             * 单曲循环
             */
            const val SINGLE_PLAY_MODE = 99

            /**
             * 随机播放
             */
            const val RANDOM_PLAY_MODE = 999
        }
    }

    /**
     * 增加历史记录
     */
    private fun addRecord(audioBean: AudioBean) {
        //加入历史
        GlobalScope.launch(Dispatchers.IO) {
            //由于audioBean可能不包含主键,故通过id查询到sortId然后再删除
            AppDataBase.getInstance()
                .historyAudioDao()
                .findAudioById(audioBean.id)
                ?.apply {
                    AppDataBase.getInstance()
                        .historyAudioDao()
                        .deleteAudio(this)
                }
            //插入数据
            AppDataBase.getInstance()
                .historyAudioDao()
                .insertAudio(HistoryAudioBean.audio2History(audioBean))
        }

        //同步内存中列表，先将原纪录移除
        for (index in 0 until _historyList.size) {
            if (_historyList[index].id == audioBean.id) {
                _historyList.remove(_historyList[index])
                break
            }
        }
        //将新记录加入到末尾
        _historyList.add(audioBean)
    }

    /**
     * 更新网络音频播放地址
     */
    fun updatePath() {
        GlobalScope.launch {
            AppDataBase.getInstance()
                .historyAudioDao()
                //查询列表中所有网络音频
                .findAudioByPathType(true)
                ?.apply {
                    AppDataBase.getInstance()
                        .historyAudioDao()
                        .updateAudios(this.onEach {
                            it.path = KuGouServer.create2()
                                .searchMusic(it.kugouAid, it.kugouHash).data.play_url
                        })
                }
            //同步内存中列表播放地址
            for (index in 0 until _historyList.size) {
                if (_historyList[index].pathType) {
                    _historyList[index].path =
                        KuGouServer.create2().searchMusic(
                            _historyList[index].kugouAid,
                            _historyList[index].kugouHash
                        ).data.play_url
                }
                Log.i("更新后播放地址", "updatePath: ${_historyList[index].path}")
            }
        }
    }

    /**
     * 获取指定范围内的随机数
     */
    private fun getRandom(start:Int,end:Int):Int{
        return ((start+Math.random()*(end-start+1)).toInt())
    }

}