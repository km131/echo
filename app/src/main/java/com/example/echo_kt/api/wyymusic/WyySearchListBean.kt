package com.example.echo_kt.api.wyymusic

import com.example.echo_kt.data.SearchBean
import com.google.gson.annotations.SerializedName

data class WyySearchListBean(
    @field:SerializedName("code") val code: String,
    @field:SerializedName("result") val result: Result
) {
    data class Result(
        @field:SerializedName("songs") val songs: List<Song>,
        @field:SerializedName("songCount") val songCount: String
    ) {
        data class Song(
            @field:SerializedName("name") val name: String,
            @field:SerializedName("id") val mid: String,
            @field:SerializedName("ar") val author: List<Author>,
            @field:SerializedName("al") val album: Album
        ): SearchBean  {
            data class Author(
                @field:SerializedName("id") val id: String,
                @field:SerializedName("name") val name: String
            )

            data class Album(
                @field:SerializedName("id") val id: String,
                @field:SerializedName("name") val name: String,
                @field:SerializedName("picUrl") val picUrl: String,
                @field:SerializedName("pic_str") val pic_str: String,
                @field:SerializedName("pic") val pic: String
            )

            override fun getId(): String = mid+album
        }
    }
}
data class WyyPathBean(val data:List<Data>,val code: String){
    data class Data(val id: String,val url:String,val size:String,val type:String)
}
