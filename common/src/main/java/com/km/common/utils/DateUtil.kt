package com.km.common.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Formatter
import java.util.Locale
import java.util.Date

/**
 * @param duration 总秒数
 * @return 时：分：秒
 */
fun stringForTime(duration: Long): String {
    val totalSeconds = duration / 1000
    val seconds: Int = (totalSeconds % 60).toInt()
    val minutes: Int = ((totalSeconds / 60) % 60).toInt()
    val hour: Int = (totalSeconds / 3600).toInt()
    return Formatter().format("%02d:%02d:%02d", hour, minutes, seconds).toString()
}

/**
 * @return 返回当前时间 yyyy年MM月dd日 HH:mm:ss
 */
fun getDate(): String {
    val simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return simpleDateFormat.format(LocalDateTime.now())
}

/**
 * @param dateTime yyyy-MM-dd ,需要查询的日期
 * @return 所在星期的第几天
 */
private fun getDayOfWeek(dateTime: String): Int {
    val cal: Calendar = Calendar.getInstance()
    if (dateTime == "") {
        cal.time = Date(System.currentTimeMillis())
    } else {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: Date = sdf.parse(dateTime)
        cal.time = Date(date.time)
    }
    return cal.get(Calendar.DAY_OF_WEEK)
}

/**
 * @param dateTime yyyy-MM-dd ,需要查询的日期
 * @return 星期几，按每星期第一天为星期日计算
 */
private fun getWeek(dateTime: String): String {
    val week = when (getDayOfWeek(dateTime)) {
        1 -> "星期日"
        2 -> "星期一"
        3 -> "星期二"
        4 -> "星期三"
        5 -> "星期四"
        6 -> "星期五"
        7 -> "星期六"
        else -> "ERROR"
    }
    return week
}


