package com.example.echo_kt.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.room.AppDataBase
import com.example.echo_kt.data.AudioBean
import com.example.echo_kt.ui.main.HistoryAudioBean
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * 读取本地音频列表
 */
fun readLocalPlayList(context: Context): MutableList<AudioBean> {
    val audioList = mutableListOf<AudioBean>()
    val cursor: Cursor? = context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        null,null,null,
        MediaStore.Audio.Media.DEFAULT_SORT_ORDER
    )
    if (cursor != null) {
        while (cursor.moveToNext()) {
            val audioBean = AudioBean()
            audioBean.name =
                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            audioBean.id ="local/"+ cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
            audioBean.singer =
                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            audioBean.path =
                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            audioBean.duration =
                cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
            audioBean.size =
                cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE))
            audioBean.albumIdUrl =
                cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
            if (audioBean.duration > 60000) {
                audioList.add(audioBean)
            }
            Log.i("TAG", "readLocalPlayList: ${audioBean.path}")
        }
        cursor.close()
    }
    return audioList
}

/**
 * 读取历史列表
 */
fun readHistoryPlayList(): MutableList<AudioBean> {
    AppDataBase.getInstance().historyAudioDao().getAllAudios()?.let {
        return HistoryAudioBean.historyList2AudioList(it)
    }?: return mutableListOf()
}
/**
 * 读取所有自定义列表
 */
fun readCustomPlayList(): MutableList<SongListBean> {
    AppDataBase.getInstance().customSongListDao().getAllAudioLists()?.let {
        return it
    }?: return mutableListOf()
}

/**
 * 写入音频文件
 */
fun writeResponseBodyToDisk(body: ResponseBody?, fileName: String) :Boolean {
    try {
        val uri=getAudioUrl("$fileName.mp3")
        Log.i("文件路径", "writeResponseBodyToDisk: $uri")
        //初始化输入流
        var inputStream : InputStream? = null
        //初始化输出流
        var outputStream : OutputStream? = null

        try {
            //设置每次读写的字节
            val fileReader = ByteArray(4096)

            var fileSizeDownloaded = 0;
            //请求返回的字节流
            inputStream = body!!.byteStream()
            //创建输出流
            outputStream =  BaseApplication.getContext().contentResolver.openOutputStream(uri!!)
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
        } catch ( e: IOException) {
            return false
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    } catch ( e: IOException) {
        Toast.makeText(BaseApplication.getContext(),"此功能暂未适配android10以下机型", Toast.LENGTH_SHORT).show()
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


