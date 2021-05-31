package com.example.echo_kt.api

/**
 * 请求歌曲详情，带歌词
 * https://www.kugou.com/yy/index.php?r=play/getdata&hash= hash
 * &album_id= album_id
 * _= album_audio_id
 */
data class MusicBean(
    val hash: String,
    val singername: String,
    val songname: String,
    val album_id: String,
    val album_audio_id: Int,
    val extname: String,
    val filename: String,
    val filesize: Int
)