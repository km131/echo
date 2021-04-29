package com.example.echo_kt.data

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException


@Entity(tableName = "custom_audio_list")
@TypeConverters(Converters::class)
class SongListBean(
    /**
     * 歌单id
     */
    @ColumnInfo(name = "id") var id: String,
    /**
     * 歌单名
     */
    @ColumnInfo(name = "listName") var name: String,
    /**
     * 创建日期
     */
    @ColumnInfo(name = "listDate") var date: String,
    /**
     * 歌曲列表
     */
    @ColumnInfo(name = "songList") var list: MutableList<AudioBean>?,
    /**
     * 歌单封面地址
     */
    @ColumnInfo(name = "coverImage") var coverImage: String
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sortId")
    var sortId: Long = 0

}

//写一个转化类，需要两个方法可以互相转化这个自定义类，转化为基本数据类型，也就是可以存入数据库的
class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): MutableList<AudioBean>? {
        // 使用Gson方法把json格式的string转成List
        try {
            return Gson().fromJson<List<AudioBean>?>(
                value,
                object : TypeToken<List<AudioBean?>?>() {}.type
            )?.toMutableList()
        } catch (e: JSONException) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    fun dateToTimestamp(date: MutableList<AudioBean>?): String? {
        return Gson().toJson(date)
    }
}
