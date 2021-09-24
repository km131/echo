package com.example.echo_kt.api.migu

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import com.example.echo_kt.BaseApplication
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.HashMap


class Migu {
     private var b :String = String()
     private var g :String = String()
     private var c :String = String()
     private var d :String = String()
     private var e :String = String()
     private var f :String = String()
     private var i :String = String()

    fun d(arg3: String): String {
        return try {
            if (TextUtils.isEmpty(arg3)) "" else Base64.encodeToString(arg3.toByteArray(), 0)
                .replace("\r|\n", "").trim()
        } catch (v0: Exception) {
            arg3
        }
    }
    fun b(arg: String): String  {
        var v2:Int
        if(TextUtils.isEmpty(arg)) {
            return ""
        }

        try {
            val v3 = MessageDigest.getInstance("MD5").digest(arg.toByteArray())
            var v0_1 = ""
            v2 = 0
            while(true) {
                if(v2 >= v3.size) {
                    return v0_1
                }

                var v1:String = Integer.toHexString(v3[v2].toInt() and 0xFF)
                if(v1.count() == 1) {
                    v1 = "0$v1"
                }

                v0_1 += v1
                ++v2
            }
        }
        catch(v0:Exception) {
            Log.e("Utils", "md5 error" + v0.localizedMessage)
            return ""
        }

    }

    /**
     * devicedId
     */
    @SuppressLint("HardwareIds")
    fun j(): String {
        val v0_3: MessageDigest?
        return try {
            if (!TextUtils.isEmpty(b)) {
                return b
            }
            val v3: K? = K.a(BaseApplication.getContext())
            if (!TextUtils.isEmpty(v3!!.a("key_device_id"))) {
                b = v3.a("key_device_id")
                return b
            }
            val v01: String = g
            val v2 =
                "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 +
                        Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +
                        Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
                        Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +
                        Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +
                        Build.TAGS.length % 10 + Build.TYPE.length % 10 +
                        Build.USER.length % 10
            val v4: String = Settings.Secure.getString(
                BaseApplication.getContext().applicationContext
                    .contentResolver, "android_id"
            )
            val v5: String = l()
            BluetoothAdapter.getDefaultAdapter()
            val v7 = StringBuilder()
            v7.append(v01).append(v2).append(v4).append(v5).append("")
            v0_3 = try {
                MessageDigest.getInstance("MD5")
            } catch (v0_2: NoSuchAlgorithmException) {
                null
            }
            if (v0_3 == null) {
                return v7.toString().toUpperCase(Locale.getDefault())
            }
            v0_3.update(v7.toString().toByteArray(), 0, v7.length)
            val v2_1: ByteArray = v0_3.digest()
            val v4_1 = StringBuilder()
            var v0_4: Int
            v0_4 = 0
            while (v0_4 < v2_1.size) {
                val v1: Int = v2_1[v0_4].toInt() and 0xFF
                if (v1 <= 15) {
                    v4_1.append("0")
                }
                v4_1.append(Integer.toHexString(v1))
                ++v0_4
            }
            val v05 = v4_1.toString().toUpperCase(Locale.getDefault())
            v3.a("key_device_id", v05)
            return v05
            v05
        } catch (v0: java.lang.Exception) {
            val v1: K = K.a(BaseApplication.getContext())
                ?: return "USSc077ba7087554560ab5d86eade004dd232aeb9e2c2d14a6cb34a4cd18a5070bd"
            b = v1.a("key_device_id")
            if (!TextUtils.isEmpty(b)) {
                return b
            }
            b = "USS5a56f017c3584e34b9f2549bc777ec31f5cb89762edc41aa89d49972eb3340d7"
            v1.a(
                "key_device_id",
                "USS5a56f017c3584e34b9f2549bc777ec31f5cb89762edc41aa89d49972eb3340d7"
            )
            "USS5a56f017c3584e34b9f2549bc777ec31f5cb89762edc41aa89d49972eb3340d7"
        }
    }
    @SuppressLint("WrongConstant", "HardwareIds")
    fun l(): String {
        if (!TextUtils.isEmpty(g)) {
            return g
        }
        try {
            val v01 =
                BaseApplication.getContext().applicationContext
                    .getSystemService("wifi") as WifiManager
            if (v01.isWifiEnabled) {
                val v02 = v01.connectionInfo
                if (v02 != null && !TextUtils.isEmpty(v02.macAddress)) {
                    g = v02.macAddress
                    return g
                }
            }
        } catch (v0: java.lang.Exception) {
            return ""
        }
        return ""
    }

    @SuppressLint("MissingPermission", "HardwareIds", "WrongConstant")
    fun h(): String {
        var v0 = ""
        if (!TextUtils.isEmpty(c)) {
            return c
        }
        try {
            v0 = (Objects.requireNonNull(
                BaseApplication.getContext().getSystemService("phone")
            ) as TelephonyManager).subscriberId
            c = v0
        } catch (v1: java.lang.Exception) {
        }
        return v0
    }
    @SuppressLint("WrongConstant")
    fun m(): String {
        if (!TextUtils.isEmpty(d)) {
            return d
        }
        try {
            val v0_1 = (Objects.requireNonNull(
                BaseApplication.getContext()
                .getSystemService("connectivity") as ConnectivityManager
            ) as ConnectivityManager).activeNetworkInfo
            //检测网络是否正常
            if (v0_1 != null && v0_1.isAvailable && v0_1.state == NetworkInfo.State.CONNECTED && v0_1.type == 0) {
                val v2 = v0_1.subtypeName
                return when (v0_1.subtype) {
                    1, 2, 4, 7, 11 -> {
                        d = "02"
                        d
                    }
                    13 -> {
                        d = "04"
                        d
                    }
                    3, 5, 6, 8, 9, 10, 12, 14, 15 -> {
                        d = "03"
                        d
                    }
                    else -> {
                        if (v2.equals("TD-SCDMA", ignoreCase = true) || v2.equals(
                                "WCDMA",
                                ignoreCase = true
                            ) || v2.equals("CDMA2000", ignoreCase = true)
                        ) {
                            d = "03"
                            return d
                        }
                        d = "01"
                        "01"
                    }
                }
            }
            d = "01"
        } catch (v0: java.lang.Exception) {
        }
        return "01"
    }
    @SuppressLint("WrongConstant")
    fun n(): String {
        if (!TextUtils.isEmpty(e)) {
            return e
        }
        try {
            val v0_1 = (BaseApplication.getContext()
                .getSystemService("connectivity") as ConnectivityManager).activeNetworkInfo
            if (v0_1 == null || !v0_1.isAvailable) {
                e = "01"
                return e
            }
            if (v0_1.type == 0 && !TextUtils.isEmpty(v0_1.extraInfo)) {
                val v0_2 = v0_1.extraInfo
                if ("cmwap".equals(v0_2, ignoreCase = true)) {
                    e = "02"
                    return e
                }
                if ("cmnet".equals(v0_2, ignoreCase = true)) {
                    e = "03"
                    return e
                }
            }
        } catch (v0: java.lang.Exception) {
        }
        e = "04"
        return e
    }

    @SuppressLint("MissingPermission", "HardwareIds", "WrongConstant")
    fun g(): String {
        var v0 = ""
        if (!TextUtils.isEmpty(f)) {
            return f
        }
        try {
            v0 = (Objects.requireNonNull(
                BaseApplication.getContext().getSystemService("phone")
            ) as TelephonyManager).deviceId
            f = v0
        } catch (v1: java.lang.Exception) {
        }
        return v0
    }
    fun e(): String {
        return Build.MODEL
    }
    fun f(): String {
        return Build.BRAND
    }
    fun c(): String {
        var v0_1: String
        val v1: Class<*> = Migu::class.java
        synchronized(v1) {
            if (TextUtils.isEmpty(i)) {
                try {
                    val v0_3: Context = BaseApplication.getContext()
                    i = v0_3.packageManager.getPackageInfo(v0_3.packageName, 0).versionName
                    return i
                } catch (v0_2: java.lang.Exception) {
                }
                v0_1 = ""
            } else {
                v0_1 = i
            }
        }
        return v0_1
    }

    /**
     * 注释掉是因为应用程序不同
     */
    fun o(): String {
//        return try {
//            val v0_1: Application = application!!
//            val v1 = v0_1.packageName
//            val v0_2: Array<Signature> = v0_1.packageManager.getPackageInfo(v1, 0x40).signatures
//            val v1_1 = MessageDigest.getInstance("MD5")
//            v1_1.update(v0_2[0].toByteArray())
//            BigInteger(1, v1_1.digest()).toString(16)
//        } catch (v0: java.lang.Exception) {
//            Log.e("TSG", "getAPPSign error:" + v0.localizedMessage)
//            ""
//        }
//    }
        return "6cdc72a439cef99a3418d2a78aa28c73"
    }
}

class K private constructor(arg3: Context?) {
    private var b: SharedPreferences? = null
    fun a(arg3: String?): String {
        return if (b == null) "" else b!!.getString(arg3, "")!!
    }

    fun a(arg3: String?, arg4: Long) {
        if (b != null) {
            val v0 = b!!.edit()
            if (v0 != null) {
                v0.putLong(arg3, arg4)
                v0.apply()
            }
        }
    }

    fun a(arg3: String?, arg4: Boolean) {
        if (b != null) {
            val v0 = b!!.edit()
            if (v0 != null) {
                v0.putBoolean(arg3, arg4)
                v0.apply()
            }
        }
    }

    fun a(arg2: String?, arg3: String?) {
        if (b != null) {
            val v0 = b!!.edit()
            if (v0 != null) {
                v0.putString(arg2, arg3)
                v0.apply()
            }
        }
    }

    fun b(arg3: String?): Boolean {
        return if (b == null) java.lang.Boolean.valueOf(false) else java.lang.Boolean.valueOf(
            b!!.getBoolean(
                arg3,
                false
            )
        )
    }

    fun c(arg4: String?): Long {
        return if (b == null) 0L else b!!.getLong(arg4, 0L)
    }

    companion object {
        private var a: K? = null
        fun a(arg3: Context?): K? {
            var v0_1: K?
            val v1: Class<*> = K::class.java
            synchronized(v1) {
                if (a == null && arg3 != null) {
                    a = K(arg3.applicationContext)
                }
                v0_1 = a
            }
            return v0_1
        }
    }

//    init {
//        if (arg3 != null) {
//            b = arg3.getSharedPreferences("com.migu.tsg.unionsearch", 0)
//        }
//    }
}
class Params{
    private val v1:HashMap<String, String> = HashMap()
    fun getMap(keyWord:String):HashMap<String, String>{
        val cx = Migu()
        val v1 = HashMap<String, String>()
        v1["deviceId"] = cx.j()
        v1["IMSI"] = cx.d(cx.h())
        v1["appId"] = "yyapp2"
        v1["mgm-Network-standard"] = cx.m()
        v1["mgm-Network-type"] =cx.n()
        v1["IMEI"] = cx.d(cx.g())
        v1["android_id"] = cx.d(UnionSearch.newInstance().androidId!!)
        v1["mac"] = cx.d(cx.l())
        v1["os"] = "android null"
        v1["osVersion"] = "android null"
        v1["platform"] = cx.e()
        v1["mode"] = "android"
        v1["brand"] = cx.f()
        v1["ua"] = "Android_migu"
        v1["ip"] = cx.d("")
        v1["uid"] = UnionSearch.newInstance().uid
        v1["version"] = cx.c()
        v1["uiVersion"] = UnionSearch.UI_VERSION
        v1["channel"] = UnionSearch.newInstance().appChannel
        v1["HWID"] = cx.d(UnionSearch.newInstance().hWID)
        v1["OAID"] = cx.d(UnionSearch.newInstance().oAID)
        //用户手机号码，合成sign的第五个参数，可以为空
        v1["msisdn"] = ""
        val v2:Long= System.currentTimeMillis()
        v1["sign"] = cx.b(keyWord+cx.o()+"yyapp2d16148780a1dcc7408e06336b98cfd50" + cx.j() + ""+ v2)
        v1["timestamp"] = v2.toString()
        return v1
    }
}


