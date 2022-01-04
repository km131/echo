package com.km.common.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.math.BigInteger
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES加密
 * @param content 需要加密的内容
 * @param key 加密秘钥
 * @return 返回加密后的内容
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun aesEncrypt(content: String, key: String, iv: String): String {
    val raw = key.toByteArray(charset("utf-8"))
    //设置密钥规范为AES
    val skeySpec = SecretKeySpec(raw, "AES")
    //"算法/模式/补码方式"
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    //CBC模式需要配置偏移量，设置一个向量，达到密码唯一性，增加加密算法的强度
    val v = IvParameterSpec(iv.toByteArray())
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, v)
    val encrypted: ByteArray = cipher.doFinal(content.toByteArray(charset("utf-8")))
    //此处使用BASE64做转码功能
    return String(Base64.getEncoder().encode(encrypted))
}

/**
 * RSA公钥加密
 * @param str 加密字符串
 * @param publicKey 公钥
 * @return 密文
 */
@RequiresApi(Build.VERSION_CODES.O)
fun rsaEncrypt(str: String, publicKey: String): String {
    //base64编码的公钥
    val decoded: ByteArray = Base64.getDecoder().decode(publicKey)
    val pubKey: RSAPublicKey = KeyFactory.getInstance("RSA").generatePublic(
        X509EncodedKeySpec(decoded)
    ) as RSAPublicKey
    //RSA加密
    val cipher: Cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.ENCRYPT_MODE, pubKey)
    val outStr = Base64.getEncoder().encode(cipher.doFinal(str.toByteArray(charset("utf-8"))))
    return String(outStr)
}
/**
 * MD5加密
 */
fun getMD5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}