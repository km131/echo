package com.example.echo_kt.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.DecimalFormat
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.R
import com.example.echo_kt.api.ProgressListener
import com.example.echo_kt.api.kugou.KuGouServer
import com.example.echo_kt.api.showToast
import com.example.echo_kt.ui.notification.CHANNEL_ID
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import okio.IOException
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
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
    return Formatter().format("%02d:%02d", minutes, seconds).toString()
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

fun CoroutineScope.downloadFile(
    fileName: String,
    url: String,
    fileType: String,
    downloadCallback: ProgressListener
) {
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
                launch(Dispatchers.IO) {
                    writeResponseBodyToDisk(responseBody, fileName, fileType)
                }
            }

            override fun onError(e: Throwable) {
                Log.e("", "onError: ${e.message}")
            }

            override fun onComplete() {
                Log.i("", "onComplete")
            }
        })
}

fun downLoadFile(url: String, fileName: String, fileType: String) {
    val downloadManager =
        BaseApplication.getContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    val request = DownloadManager.Request(Uri.parse(url))
    request.setDestinationInExternalPublicDir(
        "Music",
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.encodedPath + "/$fileName.$fileType"
    )
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    val downloadId: Long = downloadManager.enqueue(request)
}

fun updateProgress(progress: Int, title: String, maxSize: String, isEnd: Boolean) {
    val builder = NotificationCompat.Builder(BaseApplication.getContext(), CHANNEL_ID).apply {
        setContentTitle(title)
        setSmallIcon(R.mipmap.echo)
        priority = NotificationCompat.PRIORITY_LOW
    }
    NotificationManagerCompat.from(BaseApplication.getContext()).apply {
        if (isEnd) {
            builder.setContentText("$maxSize 下载完成").setProgress(0, 0, false)
        } else {
            builder.setContentText(maxSize).setProgress(100, progress, false)
        }
        notify(1025, builder.build())
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun getFileSize(size: Long): String {

    val mGB = 1024 * 1024 * 1024
    val mMB = 1024 * 1024
    val mKB = 1024
    val df = DecimalFormat("0.00")

    return when {
        size / mGB >= 1 -> df.format(size / mGB.toFloat()) + "GB"
        size / mMB >= 1 -> df.format(size / mMB.toFloat()) + "MB"
        size / mKB >= 1 -> df.format(size / mKB.toFloat()) + "KB"
        else -> size.toString() + "B"
    }
}

fun getPermission(activity: Activity, permissions: Array<String>, requestCode: Int){
    val mPermissionList: MutableList<String> = mutableListOf()
    permissions.forEach {
        if (ContextCompat.checkSelfPermission(
                BaseApplication.getContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            mPermissionList.add(it)
        }
    }
    if (mPermissionList.size > 0) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }
}
fun checkPermissions(permissions: Array<String>):Boolean{
    permissions.forEach {
        if (ContextCompat.checkSelfPermission(
                BaseApplication.getContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }
    return true
}
fun getBitmapFromUri(uri: Uri): Bitmap? {
    val parcelFileDescriptor: ParcelFileDescriptor? =
        BaseApplication.getContext().contentResolver.openFileDescriptor(uri, "r")
    val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
    val image: Bitmap? = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor?.close()
    return image
}

//在协程中调用，
fun CoroutineScope.saveBackground(uri: Uri) {
    val file = File(BaseApplication.getContext().filesDir.path + "/echo_bg.jpg")
    if (!file.exists()) {
        file.createNewFile()
    }
    val bitmap = getBitmapFromUri(uri)
    if (bitmap != null) {
        val out = FileOutputStream(file)
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            launch(Dispatchers.Main) {
                showToast("保存图片成功")
            }
        } catch (e: IOException) {
            showToast("写入内部存储失败")
        } finally {
            out.flush()
            out.close()
        }
    }
}