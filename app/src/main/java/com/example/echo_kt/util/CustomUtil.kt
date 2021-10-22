package com.example.echo_kt.util

import com.example.echo_kt.api.ProgressListener
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.R
import com.example.echo_kt.api.kugou.KuGouServer
import com.example.echo_kt.ui.notification.CHANNEL_ID
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.*
import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*


fun stringForTime(duration: Int): String {
    return stringForTime(duration.toLong())
}

fun stringForTime(duration: Long): String {
    val totalSeconds = duration / 1000
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60

    return Formatter().format("%02d:%02d", minutes, seconds).toString();
}

fun intForLong(duration: Long): Int {
    return duration.toInt()
}

fun getSongListId(name: String, date: String): String {
    return "id:标题$name,创建日期$date"
}

@SuppressLint("SimpleDateFormat")
fun getDate(): String {
    val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss") // HH:mm:ss
    return simpleDateFormat.format(Date(System.currentTimeMillis()))
}

//将mipmap转化位uri
fun getMipmapToUri(resId: Int): String {
    val r = BaseApplication.getContext().resources
    val uri = Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(resId) + "/"
                + r.getResourceTypeName(resId) + "/"
                + r.getResourceEntryName(resId)
    )
    return uri.toString()
}

//用MD5算法加密（在获取qq音乐参数sign中用到）
fun getMD5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}

//获取随机颜色RGB数组
fun getRandomColor(): Array<Float> {
    val random = Random()
    val r = random.nextInt(256).toFloat()
    val g = random.nextInt(256).toFloat()
    val b = random.nextInt(256).toFloat()
    return arrayOf(r, g, b)
}

fun downloadFile(fileName: String, url: String, downloadCallback: ProgressListener) {
    Log.i("", "下载链接: $url")
    KuGouServer.create3(downloadCallback).downloadFile(url)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnNext { Log.i("", "downloadFile: OnNext") }
        .doOnError { Log.i("", "downloadFile: OnError") }
        .observeOn(AndroidSchedulers.mainThread())
//        .unsubscribeOn(Schedulers.io())
        .subscribe(object : Observer<ResponseBody> {
            override fun onSubscribe(d: Disposable) {

            }

            @SuppressLint("CommitPrefEdits")
            override fun onNext(responseBody: ResponseBody) {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val uri = getPublicDiskFileDir("$fileName.mp3").toUri()
                        Log.i("文件路径", "writeResponseBodyToDisk: $uri")
                        //初始化输入流
                        var inputStream: InputStream? = null
                        //初始化输出流
                        var outputStream: OutputStream? = null

                        try {
                            //设置每次读写的字节
                            val fileReader = ByteArray(4096)

                            var fileSizeDownloaded = 0
                            //请求返回的字节流
                            inputStream = responseBody.byteStream()
                            //创建输出流
                            outputStream =
                                BaseApplication.getContext().contentResolver.openOutputStream(uri)
                            //进行读取操作
                            while (true) {
                                val read = inputStream.read(fileReader)

                                if (read == -1) {
                                    break
                                }
                                //进行写入操作
                                outputStream!!.write(fileReader, 0, read)
                                fileSizeDownloaded += read

                            }
                            //刷新
                            outputStream!!.flush()
                        } catch (e: IOException) {
                            Log.e("", "onNext: 文件保存失败")
                        } finally {
                            inputStream?.close()
                            outputStream?.close()
                        }
                    } catch (e: IOException) {
                        Toast.makeText(
                            BaseApplication.getContext(),
                            "此功能暂未适配android10以下机型",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {
                Log.i("", "onComplete: 下载完成")
            }
        })

//    var totalLength = "-"
//    if (file.exists()) {
//        totalLength += file.length()
//    }
//    val listener = object : ProgressResponse.com.example.echo_kt.api.com.example.echo_kt.api.ProgressListener {
//        override fun update(
//            bytesRead: Long,
//            contentLength: Long,
//            done: Boolean
//        ) {
//            //计算百分比并更新ProgressBar
//            val percent = (100 * bytesRead / contentLength).toInt()
//            updateProgress(percent)
//        }
//
//    }
//    val download: Call<ResponseBody> =
//        KuGouServer.create3(listener).downloadFile("bytes=$range$totalLength", url, object :DownloadCallBack{
//
//        })

//    download.enqueue(object : Callback<ResponseBody> {
//        override fun onResponse(
//            call: Call<ResponseBody>,
//            response: Response<ResponseBody>
//        ) {
//            //writeResponseBodyToDisk(response.body(), uri)
//            Log.i("SUCCEED", "onResponse: 请求下载成功，")
//        }
//
//        override fun onFailure(
//            call: Call<ResponseBody>,
//            t: Throwable
//        ) {
//            Log.e("ERROR", "onFailure: 请求下载失败")
//        }
//    })

}

private fun updateProgress(progress: Int) {
    val builder = NotificationCompat.Builder(BaseApplication.getContext(), CHANNEL_ID).apply {
        setContentTitle("正在下载")
        setContentText("下载进度")
        setSmallIcon(R.mipmap.echo)
        priority = NotificationCompat.PRIORITY_LOW
    }
    val PROGRESS_MAX = 100
    val PROGRESS_CURRENT = 0
    NotificationManagerCompat.from(BaseApplication.getContext()).apply {
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
        notify(1025, builder.build())
        builder.setContentText("Download complete")
            .setProgress(0, progress, false)
        notify(1025, builder.build())
    }
}