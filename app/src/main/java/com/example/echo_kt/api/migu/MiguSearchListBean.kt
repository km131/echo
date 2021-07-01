package com.example.echo_kt.api.migu

import com.example.echo_kt.data.SearchBean


data class MiguSearchListBean(
    //000000
    val code: String,
    //成功
    val info: String,
    //返回数量
    val resultNum: Int,
    val songResultData: SongResultData
): SearchBean{
    data class SongResultData(
        val result: List<ResultXX>
    ){
        data class ResultXX(
            val album: String,
            val albumId: String,
            val albumImgs: List<AlbumImg>,
            val id: String,
            val name: String,
            //原唱
            val originalSing: String?,
            val singer: String,
            val singerId: String,
            val songAliasName: String,
            val songDescs: String,
            val songId: String,
            val songName: String
        ){
            data class AlbumImg(
                val img: String,
                val imgSizeType: String
            )
        }
    }
}