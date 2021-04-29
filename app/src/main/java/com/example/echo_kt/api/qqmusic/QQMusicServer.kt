package com.example.echo_kt.api.qqmusic

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface QQMusicServer {

    /**
     * 精简版 https://c.y.qq.com/soso/fcgi-bin/client_search_cp?n=20&w=%E6%9C%89%E4%BD%95%E4%B8%8D%E5%8F%AF&loginUin=2602241712
     * @param n 请求数量
     * @param w 关键字
     * @param loginUin qq号
     */
    @GET("soso/fcgi-bin/client_search_cp")
    suspend fun searchList(
        @Query("n")
        n: String,
        @Query("w")
        w: String,
        @Query("loginUin")
        loginUin: String,
        @Query("format")
        format:String
    ): ListSearchResponse

    /**
     * 请求vk（精简版 https://u.y.qq.com/cgi-bin/musics.fcg?sign=zzakcwbo8108njf8f400a95b3bc8c447ce4113a3373804&loginUin=2602241712&data=%7B%22req%22%3A%7B%22module%22%3A%22CDN.SrfCdnDispatchServer%22%2C%22method%22%3A%22GetCdnDispatch%22%2C%22param%22%3A%7B%22guid%22%3A%22596611375%22%2C%22calltype%22%3A0%2C%22userip%22%3A%22%22%7D%7D%2C%22req_0%22%3A%7B%22module%22%3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%22%3A%22596611375%22%2C%22songmid%22%3A%5B%22004CbNym3R18Z7%22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%222602241712%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A2602241712%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D）
     * @param sign 通过getSign函数获取
     * @param loginUin qq号
     * @param data 通过getData()函数获取
     */
    @GET("cgi-bin/musics.fcg")
    suspend fun searchVKey(
        @Query("sign")
        sign: String,
        @Query("loginUin")
        loginUin: String,
        @Query("data")
        data: String
    ): GetVKeyResponse

    //请求音频文件，url是请求vk返回值的purl
    @GET
    fun getAudioFile(@Url url: String): Call<ResponseBody>

    companion object {
        //soso/fcgi-bin/client_search_cp?ct=24&qqmusic_ver=1298&new_json=1&remoteplace=txt.yqq.song&searchid=55240636747206584&t=0&aggr=1&cr=1&catZhida=1&lossless=0&flag_qc=0&p=1&n=10&w=%E6%9C%89%E4%BD%95%E4%B8%8D%E5%8F%AF&g_tk_new_20200303=713787748&g_tk=713787748&loginUin=2602241712&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0
        private const val BASE_URL = "https://c.y.qq.com/"

        private const val VKey_URL = "https://u.y.qq.com/"

         //获取歌曲详情，MP3链接
        //https://ws.stream.qqmusic.qq.com/C400003AKoyO2Otmmp.m4a?guid=596611375&vkey=FDF8F2118B43A9E4C0B6786F583248236E7AB6D3D1737E08FBB355ED9A36BB21C26315F1332C79E6A9F3F792CC6039BED92FF53A372D2DBB&uin=2602241712&fromtag=66
        private const val PATH_URL = "https://ws.stream.qqmusic.qq.com/"

        /**
         * 搜索列表和请求地址参数接口
         */
        fun create(): QQMusicServer {

            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QQMusicServer::class.java)
        }

        fun createVKey(): QQMusicServer {

            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(VKey_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QQMusicServer::class.java)
        }

        /**
         * 歌曲详情接口
         */
        fun create2(): QQMusicServer {

            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(PATH_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QQMusicServer::class.java)
        }
    }
}