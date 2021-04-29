package com.example.echo_kt.data
import com.google.gson.annotations.SerializedName

data class CustomSearchBean(
    @SerializedName("status")
    val status: Int,
    @SerializedName("error")
    val error: String,
    @SerializedName("data")
    val data: Data,
    @SerializedName("errcode")
    val errorCode: Int
):SearchBean
data class Data(
    @SerializedName("info")
    val info: List<Info>,
    @SerializedName("total")
    val totalSize: Int
)
data class Info(
    @SerializedName("album_audio_id")
    val albumAudioId: Int,
    @SerializedName("album_id")
    val albumId: String,
    @SerializedName("album_name")
    val albumName: String,
    @SerializedName("audio_id")
    val audioId: Int,
    /**
     * 比特率
     */
    @SerializedName("bitrate")
    val bitrate: Int,
    /**
     * 时间长度
     */
    @SerializedName("duration")
    val duration: Int,
    /**
     * 歌曲类型，一般为MP3
     */
    @SerializedName("extname")
    val format: String,
    /**
     * 0=免费，1=付费
     */
    @SerializedName("feetype")
    val feeType: Int,
    /**
     * 文件名，文件大小
     */
    @SerializedName("filename")
    val fileName: String,
    @SerializedName("filesize")
    val fileSize: Int,
    /**
     * 跳转歌曲详情必选参数：hash或者320hash
     */
    @SerializedName("hash")
    val hash: String,
    /**
     * 1=原创（猜测）
     */
    @SerializedName("isoriginal")
    val isOriginal: Int,
    @SerializedName("m4afilesize")
    val m4aFileSize: Int,
    @SerializedName("mvhash")
    val mvHash: String,
    /**
     * 下面两个和你想到一样，
     * 似乎没有空值，
     * 必用
     */
    @SerializedName("singername")
    val singerName: String,
    @SerializedName("songname")
    val songName: String
)
