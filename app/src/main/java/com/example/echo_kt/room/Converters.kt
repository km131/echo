package com.example.echo_kt.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException

//将自定义类转化为基本数据类型
class Converters {
    @TypeConverter
    fun fromParameter(value: String?): HashMap<String,String>? {
        // 使用Gson方法把json格式的string转成map
        try {
            return Gson().fromJson<HashMap<String,String>?>(
                value,
                object : TypeToken<HashMap<String,String>?>() {}.type
            )
        } catch (e: JSONException) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    fun dateToParameter(date: HashMap<String,String>?): String? {
        return Gson().toJson(date)
    }
}
