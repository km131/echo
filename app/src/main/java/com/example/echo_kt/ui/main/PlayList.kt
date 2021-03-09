package com.example.echo_kt.ui.main

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.example.echo_kt.BaseApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * des 播放列表
 * 关于历史和收藏.. 当历史和收藏列表需要改变时,数据库和内存中列表(手动更新)同时更新,UI与内存列表保持一致
 * 这样做的意图是避免每次操作历史/收藏列表时频繁读取数据库做数据同步
 * @author zs
 * @data 2020/6/25
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
     * 默认播放列表,本地资源
     */
    private var _localList = mutableListOf<AudioBean>()

    /**
     * 只读，下同
     */
    var localList: List<AudioBean> = _localList

    /**
     * 播放模式，默认为顺序播放
     */
    private var playMode = PlayMode.ORDER_PLAY_MODE


    init {
        //通过io线程读取播放列表
        GlobalScope.launch(Dispatchers.IO) {
            _localList = readLocalPlayList(BaseApplication.getContext())
            localList = _localList
        }
    }

    //读取本地音频列表
    public fun readLocalPlayList(context:Context): MutableList<AudioBean> {
        val audioList = mutableListOf<AudioBean>()
        val cursor:Cursor? = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,null,null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val audioBean = AudioBean()
                audioBean.name =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                audioBean.id =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                audioBean.singer =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                audioBean.path =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                audioBean.duration =
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                audioBean.size =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE))
                audioBean.albumId =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                if (audioBean.duration > 60000) {
                    audioList.add(audioBean)
                }
            }
            cursor.close()
        }
        return audioList
    }

    /**
     * 当前正在播放的音频 默认为null
     */
    private var currentAudio: AudioBean? = null

    /**
     * 当前播放音频对象在对应播放列表的角标
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
     * 下一个音频
     */
    fun nextAudio(): AudioBean? {
        if (currentAudioList.size>0) {
            when (playMode) {
                //顺序
                PlayMode.ORDER_PLAY_MODE -> {
                    currentIndex = if (currentIndex < currentAudioList.size - 1) {
                        currentIndex + 1
                    } else {
                        0
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
     * 上一个音频
     */
    fun previousAudio(): AudioBean? {
        if (currentAudioList.size>0) {
            when (playMode) {
                //顺序
                PlayMode.ORDER_PLAY_MODE -> {
                    currentIndex = if (currentIndex > 0) {
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
            //当前播放列表为空将丹铅播放置为null
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
                playMode = PlayMode.SINGLE_PLAY_MODE
            }
            PlayMode.SINGLE_PLAY_MODE -> {
                playMode = PlayMode.RANDOM_PLAY_MODE
            }
            PlayMode.RANDOM_PLAY_MODE -> {
                playMode = PlayMode.ORDER_PLAY_MODE
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
     * 之所以没有全局开放一个index,是为了尽可能的降低 index 的操作权限
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

    /**
     * 获取播放列表长度
     */
    fun getPlayListSize(): Int {
        return currentAudioList.size
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
     * 获取指定范围内的随机数
     */
    private fun getRandom(start:Int,end:Int):Int{
        return ((start+Math.random()*(end-start+1)).toInt())
    }

}