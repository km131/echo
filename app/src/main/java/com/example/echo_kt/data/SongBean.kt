package com.example.echo_kt.data

import android.graphics.Bitmap
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bumptech.glide.Glide
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.room.Converters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@TypeConverters(Converters::class)
@Entity(tableName = "all_song_list")
data class SongBean(
    /**
     * 歌曲id
     */
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String,
    /**
     * 歌曲名
     */
    @ColumnInfo(name = "songName") var songName: String,
    /**
     * 作者
     */
    @ColumnInfo(name = "author") var author: String,
    /**
     * 来源（local、qq、wyy、kugou、migu、other）
     */
    @ColumnInfo(name = "source") var source: String,
    /**
     * 播放链接
     */
    @ColumnInfo(name = "audioUrl") var audioUrl: String,
    /**
     * 专辑封面链接
     */
    @ColumnInfo(name = "albumUrl") var albumUrl: String
) {


    @Ignore
    private var bitmap: Bitmap? = null

    fun getAlbumBitmap(): Bitmap? {
        if (bitmap == null) {
            bitmap = runBlocking(Dispatchers.IO){
                Log.i("SongBean", "getAlbumBitmap: $albumUrl")
                Glide.with(BaseApplication.getContext()).asBitmap()
                    .load(albumUrl)
                    .submit()
                    .get()
            }
        }
        return bitmap
    }

    /**
     * 播放次数
     */
    @ColumnInfo(name = "playCount")
    var playCount: Long = 0

    /**
     * 最近一次修改的日期
     */
    @ColumnInfo(name = "listDate")
    var date: String = ""

    /**
     * 请求播放链接的必要参数（用于更新播放链接）
     */
    @ColumnInfo(name = "requestParameter")
    var requestParameter: HashMap<String, String>? = null

    /**
     * 是否收藏
     */
    @ColumnInfo(name = "isLike")
    var isLike: Boolean = false

    /**
     * 文件类型(后缀)
     */
    @ColumnInfo(name = "fileType")
    var fileType: String = ""

    @ColumnInfo(name = "lyric")
    var lyric: String = ""
}


