package com.example.echo_kt.api.qqmusic

import com.example.echo_kt.data.SearchBean
import com.google.gson.annotations.SerializedName

data class ListSearchResponse (
    @field:SerializedName("code") val code: String,
    @field:SerializedName("data") val data: Data
):SearchBean
data class Data(
    @field:SerializedName("song") val songList: PageList
)
data class PageList (
    @field:SerializedName("curnum") val curNum: String,
    @field:SerializedName("curpage") val curPage: String,
    @field:SerializedName("list") val data: List<AudioList>
)
data class AudioList (
    @field:SerializedName("songmid") val songmid: String,
    @field:SerializedName("songname") val songName: String,
    @field:SerializedName("singer") val singer: List<Singer>,
    @field:SerializedName("albummid") val album: String
)
data class Singer(
    @field:SerializedName("id") val id :String,
    @field:SerializedName("mid") val mediaMid: String,
    @field:SerializedName("name") val singerName: String
)
data class GetVKeyResponse(
    @field:SerializedName("code") val code :String,
    @field:SerializedName("req_0") val req: Req,
    @field:SerializedName("name") val songName: String
)
data class Req(
    @field:SerializedName("data") val midurlinfo: MidUrlInfo
)
data class MidUrlInfo(
    @field:SerializedName("midurlinfo") val reqData: List<ReqData>
)
data class ReqData(
    @field:SerializedName("purl") val purl: String,
    @field:SerializedName("songmid") val songMid: String,
    @field:SerializedName("vkey") val vKey: String,
    @field:SerializedName("filename") val fileName: String
)
