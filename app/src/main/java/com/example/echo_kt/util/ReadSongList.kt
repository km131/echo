package com.example.echo_kt.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.api.kugou.KuGouServer
import com.example.echo_kt.api.showToast
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.room.AppDataBase
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.data.SongsRepository
import com.example.echo_kt.model.QQMusicModel
import com.example.echo_kt.model.WyyMusicModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * 读取本地音频列表
 */
fun readLocalPlayList(context: Context): MutableList<SongBean> {
    val audioList = mutableListOf<SongBean>()
    val cursor: Cursor? = context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        null, null, null,
        MediaStore.Audio.Media.DEFAULT_SORT_ORDER
    )
    if (cursor != null) {
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) > 60000) {
                val audioBean = SongBean(
                    songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                    id = "local/" + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
                    author = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                    audioUrl = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
                    albumUrl = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)),
                    source = "local"
                )
                audioList.add(audioBean)
            }
        }
        cursor.close()
    }
    return audioList
}

/**
 * 读取历史列表
 */
fun readHistoryPlayList(): MutableList<SongBean> {
    val list = mutableListOf<SongBean>()
    val s = AppDataBase.getInstance().historyAudioDao().getAllSongs()
        if (!s.isNullOrEmpty()) {
            for (element in s)
                list.add(element.playlists)
        }
        return list
}
/**
 * 读取收藏列表
 */
fun readLikePlayList(songsRepository:SongsRepository): MutableList<SongBean> {
    val list = mutableListOf<SongBean>()
    val s = songsRepository.findSongByIsLike(true)
    val ss = s.asLiveData().value
    if (!ss.isNullOrEmpty()) {
        for (element in ss)
            list.add(element)
    }
    return list
}

/**
 * 写入音频文件
 */
fun writeResponseBodyToDisk(body: ResponseBody?, fileName: String): Boolean {
    try {
        val uri = getAudioUrl("$fileName.mp3")
        Log.i("文件路径", "writeResponseBodyToDisk: $uri")
        //初始化输入流
        var inputStream: InputStream? = null
        //初始化输出流
        var outputStream: OutputStream? = null

        try {
            //设置每次读写的字节
            val fileReader = ByteArray(4096)

            var fileSizeDownloaded = 0;
            //请求返回的字节流
            inputStream = body!!.byteStream()
            //创建输出流
            outputStream = BaseApplication.getContext().contentResolver.openOutputStream(uri!!)
            //进行读取操作
            while (true) {
                val read = inputStream.read(fileReader)

                if (read == -1) {
                    break
                }
                //进行写入操作
                outputStream!!.write(fileReader, 0, read);
                fileSizeDownloaded += read;

            }
            //刷新
            outputStream!!.flush();
            return true
        } catch (e: IOException) {
            return false
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    } catch (e: IOException) {
        Toast.makeText(BaseApplication.getContext(), "此功能暂未适配android10以下机型", Toast.LENGTH_SHORT)
            .show()
        return true
    }
}

/**
 * 获取公共存储空间
 */
private fun getAudioUrl(fileName: String): Uri? {
    val contentValues = ContentValues()
    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
    contentValues.put(MediaStore.Images.Media.MIME_TYPE, "audio/mp3")
    return BaseApplication.getContext().contentResolver
        .insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)
}

/**
 * 获取存储路径(包名下的file文件夹)
 */
private fun getPublicDiskFileDir(context: Context, fileName: String): String {
    var cachePath: String? = ""
    cachePath = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        || !Environment.isExternalStorageRemovable()
    ) { //此目录下的是外部存储下的私有的fileName目录
        context.getExternalFilesDir(fileName)!!.absolutePath
    } else {
        context.filesDir.path
            .toString() + "/" + fileName
    }
    val file = File(cachePath)
    if (!file.exists()) {
        file.mkdirs()
    }
    return file.absolutePath
}
suspend fun updateUrl(){
        AppDataBase.getInstance()
            .songDao()
            //查询列表中所有网络音频
            .findSongBySource("kugou")
            ?.apply {
                AppDataBase.getInstance()
                    .songDao()
                    .updateSongs(this.onEach {
                        val map = it.requestParameter!!
                        it.audioUrl = KuGouServer.create2()
                            .searchMusic(map["id"]!!, map["hash"]!!).data.play_url
                    })
            }
        AppDataBase.getInstance()
            .songDao()
            //查询列表中所有网络音频
            .findSongBySource("wyy")
            ?.apply {
                AppDataBase.getInstance()
                    .songDao()
                    .updateSongs(this.onEach {
                        val map = it.requestParameter!!
                        it.audioUrl = WyyMusicModel().getSongPath(map["musicId"]!!)!!.data[0].url
                    })
            }
        AppDataBase.getInstance()
            .songDao()
            //查询列表中所有网络音频
            .findSongBySource("qq")
            ?.apply {
                AppDataBase.getInstance()
                    .songDao()
                    .updateSongs(this.onEach {
                        val map = it.requestParameter!!
                        it.audioUrl = QQMusicModel().getPath(map["mid"]!!)
                    })
            }
}

