package com.example.echo_kt.api.kugou

data class SearchMusicDetails(
    val `data`: Data,
    val err_code: Int,
    val status: Int
){
    data class Data(
        val album_id: String,
        val album_name: String,
        val audio_id: String,
        val audio_name: String,
        val author_id: String,
        val author_name: String,
        val authors: List<Author>,
        val bitrate: Int,
        val filesize: Int,
        val has_privilege: Boolean,
        val hash: String,
        val have_album: Int,
        val have_mv: Int,
        val img: String,
        val is_free_part: Int,
        val lyrics: String,
        val play_backup_url: String,
        val play_url: String,
        val privilege: Int,
        val privilege2: String,
        val recommend_album_id: String,
        val song_name: String,
        val timelength: Int,
        val video_id: Int
    ){
        data class Author(
            val author_id: String,
            val author_name: String,
            val avatar: String,
            val is_publish: String,
            val sizable_avatar: String
        )
    }
}