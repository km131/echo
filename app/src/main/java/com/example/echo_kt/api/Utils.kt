package com.example.echo_kt.api

import android.util.Base64
import android.widget.Toast
import com.example.echo_kt.BaseApplication
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


fun getBase64(string: String):String{
    return Base64.encodeToString(string.toByteArray(StandardCharsets.UTF_8),0)
}
fun md5(string: String):String{
    val v3:ByteArray = MessageDigest.getInstance("MD5").digest(string.toByteArray(StandardCharsets.UTF_8))
    var v01 = ""
    var v2 = 0
    while (true){
        if (v2 >= v3.size){
            return v01
        }
        var v1:String = Integer.toHexString(v3[v2].toInt() and 0xff)
        if (v1.length ==1){
            v1 = "0$v1"
        }
        v01 += v1
        v2++
    }
}

fun hexStringToBytes(hexString: String?): ByteArray? {
    var hexString = hexString
    if (hexString == null || hexString == "") {
        return null
    }
    hexString = hexString.toUpperCase()
    val length = hexString.length / 2
    val hexChars = hexString.toCharArray()
    val d = ByteArray(length)
    for (i in 0 until length) {
        val pos = i * 2
        d[i] = ((hexChars[pos]).toInt() shl 4 or (hexChars[pos + 1]).toInt()).toByte()
    }
    return d
}
fun showToast(text:String){
    Toast.makeText(
        BaseApplication.getContext(),
        text,
        Toast.LENGTH_SHORT
    ).show()
}

