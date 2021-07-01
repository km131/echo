package com.example.echo_kt.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.echo_kt.api.wyymusic.WyyMusicServer
import com.example.echo_kt.api.wyymusic.WyyPathBean
import com.example.echo_kt.api.wyymusic.WyySearchListBean
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.data.WyyParameter
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class WyyMusicModel {

    private var wyyMusicServer: WyyMusicServer = WyyMusicServer.create()

    suspend fun getSearchList(keyword: String): WyySearchListBean? {
        val key =
            "{\"hlpretag\":\"<span class=\\\"s-fc7\\\">\",\"hlposttag\":\"</span>\",\"s\":\"$keyword\",\"type\":\"1\",\"offset\":\"0\",\"total\":\"true\",\"limit\":\"30\",\"csrf_token\":\"\"}"
        val params = getBody(key)
        return try {
            wyyMusicServer.searchList(params.params, params.encSecKey)
        }catch (e:Exception){
            null
        }
    }

    suspend fun getSongPath(musicId: String): WyyPathBean? {
        val key =
            "{\"ids\":\"[$musicId]\",\"level\":\"standard\",\"encodeType\":\"aac\",\"csrf_token\":\"\"}"
        val params = getBody(key)
        return try {
            wyyMusicServer.searchPath(params.params, params.encSecKey)
        }catch (e:Exception){
            null
        }
    }

    fun convertSongBean(pathBean:WyyPathBean.Data, listBean:WyySearchListBean.Result.Song):SongBean{
        return SongBean(
            songName = listBean.name,
            author = listBean.author[0].name,
            albumUrl = listBean.album.picUrl,
            audioUrl = pathBean.url,
            id = "WyyMusic/${listBean.id}",
            source = "wyy"
        ).apply {
            val parameterMap = HashMap<String,String>()
            parameterMap["musicId"]=listBean.id
            requestParameter = parameterMap
        }
    }

    private fun getBody(p1:String) :WyyParameter{
        //第一次加密密钥
        val param4 = "0CoJUm6Qyw8W8jud"
        //第二次加密密钥
        val randNum = "ZdJxaMrFu6rPNMgi"
        val vi = "0102030405060708"
        var encText = aesEncrypt(p1, param4,vi).toByteArray()
        encText =  aesEncrypt(String(encText), randNum, vi).toByteArray()
        val params = String(encText)
        //只根据第二次加密密钥变化，此处密钥固定，所以此参数也固定
        val encSecKey = "7516fe7bd0cb0e911036c61c45618f98fa6258a82bfa187d256c729546c9ca0f6a622d26bb75cbb45767c0e2d7e02190a227ee9ff6a7ed37639cd5c76aa943ced445be70421e564d34e4bcf3bd35722af86a41e5eed728848d9a7e784cae79bf5235d9cd9830d97df4db705b35cdb5dfc37317875ef796623d0e519f7726098d"
//        Log.i("TAG", "params = $params")
//        Log.i("TAG", "encSecKey = $encSecKey")
        return WyyParameter(params,encSecKey)
    }

    /**
	 * AES加密
	 * @param content 需要加密的内容
	 * @param key 加密秘钥
	 * @return 返回加密后的内容
	 */
    @Throws(Exception::class)
    fun aesEncrypt(content: String, key: String?, iv: String): String {
        val raw = key!!.toByteArray(charset("utf-8"))
        val skeySpec =
            SecretKeySpec(raw, "AES") //设置密钥规范为AES
        val cipher =
            Cipher.getInstance("AES/CBC/PKCS5Padding") //"算法/模式/补码方式"
        //CBC模式需要配置偏移量，设置一个向量，达到密码唯一性，增加加密算法的强度
        val v = IvParameterSpec(iv.toByteArray())
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, v)
        val encrypted:ByteArray = cipher.doFinal(content.toByteArray(charset("utf-8")))
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String(Base64.getEncoder().encode(encrypted)) //此处使用BASE64做转码功能
        } else {
            ""
        }
    }

    /**
     * RSA公钥加密
     * @param str 加密字符串
     * @param publicKey 公钥
     * @return 密文
     */
    @RequiresApi(Build.VERSION_CODES.O)
     fun rsaEncrypt(str:String, publicKey:String ):String{
        //base64编码的公钥
        val decoded:ByteArray = Base64.getDecoder().decode(publicKey)
        val pubKey : RSAPublicKey = KeyFactory.getInstance("RSA").generatePublic(
            X509EncodedKeySpec(decoded)
        ) as RSAPublicKey
        //RSA加密
        val cipher:Cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, pubKey)
        val outStr = Base64.getEncoder().encode(cipher.doFinal(str.toByteArray(charset("utf-8"))))
        return String(outStr)
    }
}