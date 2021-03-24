package com.example.echo_kt.api

import com.google.gson.annotations.SerializedName

/**
 * 以下类采用 [Json to Kotlin class] 插件生成。故只有某些重要值有注释
 */

/**
 * 正常情况
 * status:1
 * errCode: 0
 * error: ""
 */
data class SearchBean(
    @SerializedName("status")
    val status: Int,
    @SerializedName("error")
    val error: String,
    @SerializedName("data")
    val data: Data,
    @SerializedName("errcode")
    val errcode: Int
)

data class Data(
    val aggregation: List<Aggregation>,
    val allowerr: Int,
    val correctiontip: String,
    val correctiontype: Int,
    val forcecorrection: Int,
    val info: List<Info>,
    val istag: Int,
    val istagresult: Int,
    val tab: String,    //暂时只发现 ""和"全部"
    val timestamp: Int,
    val total: Int
)

/**
 * 正常情况
 * count: 0
 * key:歌曲场景（DJ，现场，广场舞，伴奏，铃声），貌似只有这5个
 */
data class Aggregation(
    val count: Int,
    val key: String
)

//应该是搜索状态下的每个列表的信息
data class Info(
    //320据说表示资源暂时转移，根据hash跳转
    val `320filesize`: Int,
    val `320hash`: String,
    val `320privilege`: Int,
    val Accompany: Int,
    /**
     *  audio_id：6-9位数
     *  必用
     *  id、name可能为空
     */
    val album_audio_id: Int,
    val album_id: String,
    val album_name: String,
    /**
     * 6-9位数
     */
    val audio_id: Int,
    val bitrate: Int,
    val duration: Int,
    /**
     * 歌曲类型，一般为MP3
     * 必用
     */
    val extname: String,
    val fail_process: Int,
    val fail_process_320: Int,
    val fail_process_sq: Int,
    val feetype: Int,
    /**
     * 文件名，文件大小
     * 必用
     */
    val filename: String,
    val filesize: Int,
    val fold_type: Int,
    //暂时不用
    val group: List<Group>,
    /**
     * 跳转歌曲详情必选参数：hash或者320hash
     * 必用
     */
    val hash: String,
    val isnew: Int,
    val isoriginal: Int,
    val m4afilesize: Int,
    val mvhash: String,
    val old_cpy: Int,
    val othername: String,
    //原始名字？多为空值
    val othername_original: String,
    val ownercount: Int,
    val pay_type: Int,
    val pay_type_320: Int,
    val pay_type_sq: Int,
    val pkg_price: Int,
    val pkg_price_320: Int,
    val pkg_price_sq: Int,
    val price: Int,
    val price_320: Int,
    val price_sq: Int,
    val privilege: Int,
    val rp_publish: Int,
    val rp_type: String,
    /**
     * 下面两个和你想到一样，
     * 似乎没有空值，
     * 必用
     */
    val singername: String,
    val songname: String,
    val songname_original: String,
    val source: String,
    val sourceid: Int,
    val sqfilesize: Int,
    val sqhash: String,
    val sqprivilege: Int,
    val srctype: Int,
    val topic: String,
    val topic_url: String,
    val trans_param: TransParamX
)

/**
 * 大多情况下为空：[],我应该用不到，故不管
 */
data class Group(
    val `320filesize`: Int,
    val `320hash`: String,
    val `320privilege`: Int,
    val Accompany: Int,
    val album_audio_id: Int,
    val album_id: String,
    val album_name: String,
    val audio_id: Int,
    val bitrate: Int,
    val duration: Int,
    val extname: String,
    val fail_process: Int,
    val fail_process_320: Int,
    val fail_process_sq: Int,
    val feetype: Int,
    val filename: String,
    val filesize: Int,
    val fold_type: Int,
    val hash: String,
    val isnew: Int,
    val isoriginal: Int,
    val m4afilesize: Int,
    val mvhash: String,
    val old_cpy: Int,
    val othername: String,
    val othername_original: String,
    val ownercount: Int,
    val pay_type: Int,
    val pay_type_320: Int,
    val pay_type_sq: Int,
    val pkg_price: Int,
    val pkg_price_320: Int,
    val pkg_price_sq: Int,
    val price: Int,
    val price_320: Int,
    val price_sq: Int,
    val privilege: Int,
    val rp_publish: Int,
    val rp_type: String,
    val singername: String,
    val songname: String,
    val songname_original: String,
    val source: String,
    val sourceid: Int,
    val sqfilesize: Int,
    val sqhash: String,
    val sqprivilege: Int,
    val srctype: Int,
    val topic: String,
    val topic_url: String,
    val trans_param: TransParam
)

/**
 * 和下面的类在返回时的名字相同，
 * 偶尔有HashOffset值，
 * 搜索冷门歌曲未出现此值
 * 注意返回值数量并不一定为全部
 */
data class TransParamX(
    val appid_block: String,
    val cid: Int,
    val cpy_attr0: Int,
    val cpy_grade: Int,
    val cpy_level: Int,
    val display: Int,
    val display_rate: Int,
    val hash_offset: HashOffset,
    val musicpack_advance: Int,
    val pay_block_tpl: Int
)

data class TransParam(
    val appid_block: String,
    val cid: Int,
    val cpy_attr0: Int,
    val cpy_grade: Int,
    val cpy_level: Int,
    val display: Int,
    val display_rate: Int,
    val musicpack_advance: Int,
    val pay_block_tpl: Int
)

data class HashOffset(
    val end_byte: Int,
    val end_ms: Int,
    val file_type: Int,
    val offset_hash: String,
    val start_byte: Int,
    val start_ms: Int
)