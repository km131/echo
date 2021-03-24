package com.example.echo_kt.util

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