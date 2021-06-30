package com.example.echo_kt.play

import com.example.echo_kt.BaseApplication
import com.example.echo_kt.room.AppDataBase
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.ui.main.HistoryAudioBean
import com.example.echo_kt.util.getDate
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
    private var currentAudioList = mutableListOf<SongBean>()

    /**
     * 本地播放列表,本地资源
     */
    private var _localList = mutableListOf<SongBean>()

    /**
     * 只读，下同
     */
    private var localList: MutableList<SongBean> = _localList

    fun getLocalList():MutableList<SongBean>{
        return localList
    }
    /**
     * 默认播放列表,历史
     */
    private var _historyList = mutableListOf<SongBean>()
    private var historyList: List<SongBean> = _historyList
    fun getHistoryList():List<SongBean>{
        return historyList
    }
    fun setHistoryList(bean:SongBean):Boolean{
        return _historyList.remove(bean)
    }

    /**
     * 播放模式，默认为顺序播放
     */
    private var playMode = PlayMode.ORDER_PLAY_MODE


    init {
        //读取播放列表
        initHistoryList()
        initPlayList()
    }

    fun initHistoryList() {
        GlobalScope.launch(Dispatchers.IO) {
            _localList = readLocalPlayList(BaseApplication.getContext())
            localList = _localList
            val s = readHistoryPlayList()
            _historyList = if (s.isNullOrEmpty()) {
                mutableListOf()
            } else s
            historyList = _historyList
        }
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
    private var currentAudio: SongBean? = null

    /**
     * 当前播放音频对象在对应播放列表的索引
     */
    private var currentIndex = 0

    /**
     * 当前正在播放的音频
     */
    fun currentAudio(): SongBean? {
        return currentAudio
    }

    /**
     * 设置当前播放列表和currentIndex
     */
    fun setCurrentAudio(audioBean: SongBean) {
        addRecord(audioBean)
        initPlayList()
        //重置当前角标
        currentIndex = getIndexByAudio(audioBean)
    }

    /**
     * 第一次进入播放的音频，默认为播放列表的第一个
     */
    fun startAudio(): SongBean? {
        currentAudio = currentAudioList[0]
        return currentAudio
    }

    /**
     * 替换播放列表
     */
    fun switchAudioList(list:MutableList<SongBean>){
        currentAudioList.apply {
            clear()
            addAll(list)
        }
    }

    /**
     * 下一个音频
     */
    fun nextAudio(): SongBean? {
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
                    currentIndex = getRandom(currentAudioList.size - 1)
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
    fun previousAudio(): SongBean? {
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
                    currentIndex = getRandom(currentAudioList.size - 1)
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
    private fun getIndexByAudio(audioBean: SongBean): Int {
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
    fun setNextPlay(audioBean: SongBean) {
        if (currentAudioList.size>0){
            currentAudioList.add(currentIndex+1,audioBean)
        }
    }

    /**
     * 获取播放列表
     */
    fun getPlayList(): MutableList<SongBean> {
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
    private fun addRecord(audioBean: SongBean) {
        //加入历史
        GlobalScope.launch(Dispatchers.IO) {
            //由于audioBean可能不包含主键,故通过id查询到sortId然后再删除
            AppDataBase.getInstance()
                .historyAudioDao()
                .findAudioById(audioBean.id)
                ?.apply {
                    AppDataBase.getInstance()
                        .historyAudioDao()
                        .deleteAudio(this.songId)
                    //插入数据
                    AppDataBase.getInstance()
                        .historyAudioDao()
                        .insertAudio(this)
                } ?: (
                    AppDataBase.getInstance()
                        .historyAudioDao()
                        .insertAudio(HistoryAudioBean(audioBean.id))
                    )

            AppDataBase.getInstance()
                .songDao()
                .findSongById(audioBean.id)
                ?.apply {
                    //插入数据
                    AppDataBase.getInstance()
                        .songDao()
                        .updateSong(this.apply {audioUrl = audioBean.audioUrl ; date = getDate() ; playCount += 1 })
                } ?: (
                    AppDataBase.getInstance()
                        .songDao()
                        .insertSong(audioBean.apply { date = getDate() ; playCount = 1 })
                    )

        }
        if (!_historyList.isNullOrEmpty()) _historyList.remove(audioBean)
        //将新记录加入到末尾
        _historyList.add(audioBean)
    }

    /**
     * 获取指定范围内的随机数
     */
    private fun getRandom(end:Int):Int{
        return ((Math.random()*(end+1)).toInt())
    }

}