package com.example.echo_kt.api.migu

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.math.BigInteger
import java.security.MessageDigest
import retrofit2.http.Url

import okhttp3.ResponseBody

import retrofit2.http.GET

interface MiguMusicServer {

    /**@param headers 请求头
     * @param feature 默认（可改）：1011000000
     * @param pageSize 默认（可改）：20
     * @param text 关键字
     * @param searchSwitch 默认：{"bit24":2}
     */
    @GET("searchAll")
    suspend fun searchList(
        @HeaderMap
        headers:Map<String,String>,
        @Query("feature")
        feature: String,
        @Query("pageSize")
        pageSize: String,
        @Query("text")
        text: String,
        @Query("searchSwitch")
        searchSwitch:String
    ): MiguSearchListBean

    @GET("v2.2")
    suspend fun searchMusicBean(
        @HeaderMap
        headers:Map<String,String>,
        @Query("albumId")
        albumId: String,
        @Query("songId")
        songId: String,
        @Query("toneFlag")
        toneFlag: String,
        @Query("resourceType")
        resourceType:String,
        @Query("lowerQualityContentId")
        lowerQualityContentId:String
    ): MiguSearchMusicBean

    @GET
    suspend fun getLyricStr(@Url url: String): ResponseBody

    companion object {
        private const val BASE_URL = "https://jadeite.migu.cn/music_search/v2/search/"
        private const val MUSIC_URL = "https://app.c.nf.migu.cn/MIGUM2.0/strategy/listen-url/"
        /**
         * 搜索列表和请求地址参数接口
         * @param type 0 搜索列表 1 歌曲详情
         */
        fun create(type:Int): MiguMusicServer {

            val url:String = if (type==0) BASE_URL else MUSIC_URL
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MiguMusicServer::class.java)
        }
    }
}