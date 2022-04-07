package com.example.echo_kt.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.DecimalFormat
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
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
import java.util.regex.Matcher
import java.util.regex.Pattern


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

//将mipmap转化为uri
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
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.encodedPath + "/$fileName$fileType"
    )
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    downloadManager.enqueue(request)
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

//获取权限
fun getPermission(activity: Activity, permissions: Array<String>, requestCode: Int) {
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

//检查权限
fun checkPermissions(permissions: Array<String>): Boolean {
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

//保存自定义背景图片
fun saveBackground(uri: Uri): Boolean {
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
            return true
        } catch (e: IOException) {
            showToast("写入内部存储失败")
        } finally {
            out.flush()
            out.close()
        }
    }
    return false
}

fun deleteFile(uri: Uri): Boolean {
    return DocumentsContract.deleteDocument(BaseApplication.getContext().contentResolver, uri)
}

fun getRealFilePath(context: Context, uri: Uri): String {
    val scheme = uri.scheme
    var data = ""
    if (scheme == null)
        data = uri.path.toString()
    else if (ContentResolver.SCHEME_FILE == scheme) {
        data = uri.path.toString()
    } else if (ContentResolver.SCHEME_CONTENT == scheme) {
        val cursor = context.contentResolver.query(
            uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null
        )
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                if (index > -1) {
                    data = cursor.getString(index)
                }
            }
            cursor.close()
        }
    }
    return data
}

//删除图库照片
fun deleteImage(imgPath: String): Boolean {
    val resolver: ContentResolver = BaseApplication.getContext().contentResolver
    val cursor: Cursor? = MediaStore.Images.Media.query(
        resolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        arrayOf(MediaStore.Images.Media._ID),
        MediaStore.Images.Media.DATA + "=?",
        arrayOf(imgPath),
        null
    )
    return if (null != cursor && cursor.moveToFirst()) {
        val id: Long = cursor.getLong(0)
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val uri = ContentUris.withAppendedId(contentUri, id)
        val count: Int = BaseApplication.getContext().contentResolver.delete(uri, null, null)
        count == 1
    } else {
        val file = File(imgPath)
        file.delete()
    }
}

fun getLongTimex(min: String, sec: String, mill: String): Long {
    val m = Integer.parseInt(min)
    val s = Integer.parseInt(sec)
    val ms = Integer.parseInt(mill)
    if (s >= 60) {
        Log.v("", "--> [" + min + ":" + sec + "." + mill.substring(0, 2) + "]")
    }
    // 组合成一个长整型表示的以毫秒为单位的时间
    val time = m * 60 * 1000 + s * 1000 + ms
    return time.toLong()
}

fun getLrcList(lrcStr: String): MutableMap<Long, String> {
    val map = mutableMapOf<Long, String>()
    Log.i("TAG", "lrcStr:$lrcStr ")
    val pattern: Pattern = Pattern.compile("\\[(\\d{2}:\\d{2}\\.\\d{2})][^\\[]*")
    val matcher: Matcher = pattern.matcher(lrcStr)
    while (matcher.find()) {
        val s = matcher.group()
        val min = s.substring(1, 3)
        val sec = s.substring(4, 6)
        val mill = s.substring(7, 9)
        val time = getLongTimex(min, sec, mill)
        val text = s.substring(10, s.length)
        map[time] = text
        //Log.i("TAG", "getLrcList: $s , time = $time , text = $text")
    }
    return map
}

fun getBlurBitmap(context: Context, radius: Int, bitmap: Bitmap): Bitmap {
    val renderScript = RenderScript.create(context)
    val input: Allocation = Allocation.createFromBitmap(renderScript, bitmap)
    val output: Allocation = Allocation.createTyped(renderScript, input.type)
    val scriptIntrinsicBlur: ScriptIntrinsicBlur =
        ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    scriptIntrinsicBlur.setRadius(radius.toFloat())
    scriptIntrinsicBlur.setInput(input)
    scriptIntrinsicBlur.forEach(output)
    output.copyTo(bitmap)
    return bitmap
}