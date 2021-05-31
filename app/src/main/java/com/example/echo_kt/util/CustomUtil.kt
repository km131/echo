package com.example.echo_kt.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.api.SearchMusicDetails
import com.example.echo_kt.data.Info
import com.example.echo_kt.data.AudioBean
import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

fun stringForTime(duration: Int): String? {
   return stringForTime(duration.toLong())
}
fun stringForTime(duration: Long): String? {
    val totalSeconds = duration/1000
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds/60)%60

    return Formatter().format("%02d:%02d",minutes,seconds).toString();
}
fun intForLong(duration: Long):Int{
    return duration.toInt()
}
fun dataToAudioBean(
    data: SearchMusicDetails.Data,
    item: Info
): AudioBean {
    //用请求到的hash值来代表歌曲在数据库中的唯一id
    return AudioBean(
        name = data.song_name,
        singer = item.singerName,
        size = data.filesize.toLong(),
        duration = if (data.is_free_part != 1) data.timelength - 1000 else 59000,
        path = data.play_backup_url,
        albumId = data.img,
        songId = "kugou/${data.hash}",
        pathType = true,
        kugouAid = data.album_id,
        kugouHash = data.hash
    )
}
//
fun getSongListId(name:String,date:String): String {
    return "id:标题$name,创建日期$date"
}
@SuppressLint("SimpleDateFormat")
fun getDate():String{
    val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss") // HH:mm:ss
    return simpleDateFormat.format(Date(System.currentTimeMillis()))
}
//将mipmap转化位uri
fun getMipmapToUri(resId:Int):String {
    val r = BaseApplication.getContext().resources
    val uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
            + r.getResourcePackageName(resId) + "/"
            + r.getResourceTypeName(resId) + "/"
            + r.getResourceEntryName(resId));

    return uri.toString();
}

//用MD5算法加密（在获取qq音乐参数sign中用到）
fun getMD5(input:String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}