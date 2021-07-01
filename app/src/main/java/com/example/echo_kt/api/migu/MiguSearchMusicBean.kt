package com.example.echo_kt.api.migu

data class MiguSearchMusicBean(
    val code: String,
    val data: Data,
    val info: String
){
    data class Data(
        //音质
        val formatType: String,
        val songItem: SongItem,
        //该id所对应的歌曲链接
        val url: String
    ){
        data class SongItem(
            val album: String,
            val albumId: String,
            val albumImgs: List<AlbumImg>,
            val chargeAuditions: String,
            val landscapImg: String,
            //时长
            val length: String,
            //歌词链接
            val lrcUrl: String,
            val singer: String,
            val singerId: String,
            val songAliasName: String,
            val songDescs: String,
            val songId: String,
            val songName: String,
            //支持的最高音质
            val topQuality: String,
            //是否原唱
            val validStatus: Boolean
        ){

            data class AlbumImg(
                val fileId: String,
                val img: String,
                val imgSizeType: String
            )
        }
    }
}

