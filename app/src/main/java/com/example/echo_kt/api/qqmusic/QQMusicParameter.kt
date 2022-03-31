package com.example.echo_kt.api.qqmusic

import com.example.echo_kt.utils.getMD5
import kotlin.random.Random

class QQMusicParameter {
    private val encNonce="CJBPACrRuNy7"
    private val signPrxfix="zza"
    private val dir="0234567890abcdefghijklmnopqrstuvwxyz".toCharArray()

    fun getData(songMid: String): String {
        val data = "{\"req\":{\"module\":\"CDN.SrfCdnDispatchServer\",\"" +
                "method\":\"GetCdnDispatch\",\"" +
                "param\":{\"guid\":\"596611375\"," +
                "\"calltype\":0,\"userip\":\"\"}}," +
                "\"req_0\":{\"module\":\"vkey.GetVkeyServer\"" +
                ",\"method\":\"CgiGetVkey\",\"param\":{\"guid\"" +
                ":\"596611375\",\"songmid\":[\"$songMid\"]," +
                "\"songtype\":[0],\"uin\":\"1233122131\",\"loginflag\":1," +
                "\"platform\":\"20\"}},\"comm\":{\"uin\":1233122131,\"" +
                "format\":\"json\",\"ct\":24,\"cv\":0}}"
        return String(data.toByteArray(), charset("UTF-8"))
    }

    /**
     * @param data 需要加密的参数，这是一段请求体数据，为json字符串格式，例如上面的格式，可以抓包获取
     * @return 加密的方式为固定字串 zza加上一个10-16位的随机字符串再加上 固定字串 CJBPACrRuNy7加上请求数据拼接的 MD5值
     */
    fun getSign( data:String ): String {
        return signPrxfix+uuidGenerate()+ getMD5(encNonce+data)
    }
    private fun uuidGenerate():String{
        val minLen=10
        val maxLen=16
        val ran=Random(System.currentTimeMillis())
        val ranLen=ran.nextInt(maxLen-minLen)+minLen
        val sb = StringBuilder(ranLen)
        for (index in 0..minLen){
            sb.append(dir[ran.nextInt(dir.size)])
        }
        return sb.toString()
    }
}