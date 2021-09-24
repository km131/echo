package com.example.echo_kt.api.migu

import com.example.echo_kt.data.SearchBean
import com.google.gson.annotations.SerializedName


data class MiguSearchListBean(
    //000000
    val code: String,
    //成功
    val info: String,
    //返回数量
    val resultNum: Int,
    val songResultData: SongResultData
){
    data class SongResultData(
        val result: List<ResultXX>
    ){
        data class ResultXX(
            val album: String,
            val albumId: String,
            val albumImgs: List<AlbumImg>,
            @SerializedName("id")
            val sid: String,
            val name: String,
            //原唱
            val originalSing: String?,
            val singer: String,
            val singerId: String,
            val songAliasName: String,
            val songDescs: String,
            val songId: String,
            val songName: String
        ):SearchBean{
            data class AlbumImg(
                val img: String,
                val imgSizeType: String
            )

            override fun getId(): String = albumId+sid
        }
    }
}