package com.km.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.IOException

/**
 * 获取bitmap资源路径
 */
fun getBitmapFromUri(uri: Uri,context: Context): Bitmap? {
    val parcelFileDescriptor: ParcelFileDescriptor? = context.contentResolver.openFileDescriptor(uri, "r")
    val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
    val image: Bitmap? = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor?.close()
    return image
}

/**
 * 保存图片
 */
fun saveImage(uri: Uri,context: Context,pathName:String,callback:(isSuccess:Boolean)->Unit) {
    val file = File(pathName)
    if (!file.exists()) {
        file.createNewFile()
    }
    val bitmap = getBitmapFromUri(uri,context)
    if (bitmap != null) {
        val out = FileOutputStream(file)
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            callback.invoke(true)
        } catch (e: IOException) {
            callback.invoke(false)
        } finally {
            out.flush()
            out.close()
        }
    }
}