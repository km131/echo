package com.example.echo_kt.api.migu

import android.app.Application
import android.text.TextUtils
import com.example.echo_kt.BaseApplication

class UnionSearch private constructor() {

    val androidId: String?
        get() {
            if (TextUtils.isEmpty(Companion.androidId)) {
                Companion.androidId = ""
            }
            return Companion.androidId
        }
    val appChannel: String
        get() {
//            if (TextUtils.isEmpty(Companion.appChannel)) {
//                Companion.appChannel = ""
//            }
//            return Companion.appChannel
            return ""
        }
    val hWID: String
        get() {
            if (TextUtils.isEmpty(hwid)) {
                hwid = ""
            }
            return hwid
        }
    val oAID: String
        get() {
            if (TextUtils.isEmpty(oaid)) {
                oaid = ""
            }
            return oaid
        }
    /**
     * uid可以为空
     */
    val uid: String get() = ""
        //        if(TextUtils.isEmpty(UnionSearch.uid)) {
//            UnionSearch.uid = com.migu.tsg.a.g(UnionSearch.mApplicationContext);
//        }
//
//        return UnionSearch.uid;


    companion object {
        fun newInstance() = UnionSearch()
        var UI_VERSION: String = "A_music_3.5.0"
        private var androidId: String = ""
        private var hwid: String = ""
        private var oaid: String = ""

//        fun init(arg2: Application) {
//            application = arg2
//            val v0 = K.a(arg2)!!.a("key_env_config")
//            if (!TextUtils.isEmpty(v0) && "*#prs#*" == v0) {
//                SEARCH_URL = "https://jadeite.migu.cn:8030/music_search"
//                UI_VERSION = "Test_A_music_3.5.0"
//                return
//            }
//            if (TextUtils.equals(v0, "*#testrs#*")) {
//                SEARCH_URL = "http://39.156.1.73:8080/test/music_search"
//                UI_VERSION = "Test_A_music_3.5.0"
//                return
//            }
//            if (!TextUtils.isEmpty(v0) && "*#devrs#*" == v0) {
//                SEARCH_URL = "http://39.156.1.73:8080/music_search"
//                UI_VERSION = "Dev_A_music_3.5.0"
//                return
//            }
//            SEARCH_URL = "https://jadeite.migu.cn/music_search"
//            UI_VERSION = "A_music_3.5.0"
//        }
        //    public void register(OnSearchEventListener arg1) {
        //        UnionSearch.mOnSearchEventListener = arg1;
        //    }
        //    public void search24Bit(Context arg4, String arg5) {
        //        Intent v0 = new Intent(arg4, MultipleTypeSearchActivity.class);
        //        v0.putExtra("search_word", arg5);
        //        v0.putExtra("type", "24bit");
        //        arg4.startActivity(v0);
        //    }
        //
        //    public void searchAll(Context arg3, Bundle arg4) {
        //        Intent v0 = new Intent(arg3, SearchActivity.class);
        //        v0.putExtras(arg4);
        //        arg3.startActivity(v0);
        //    }
        //
        //    public void searchAll(Context arg3, String arg4, String arg5, String arg6) {
        //        Intent v0 = new Intent(arg3, SearchActivity.class);
        //        v0.putExtra("search_word", arg4);
        //        v0.putExtra("title", arg5);
        //        v0.putExtra("sub_title", arg6);
        //        arg3.startActivity(v0);
        //    }
        //
        //    public void searchConcert(Context arg3) {
        //        arg3.startActivity(new Intent(arg3, UnionSearchConcertActivity.class));
        //    }
        //
        //    public void searchMv1080(Context arg4, String arg5) {
        //        Intent v0 = new Intent(arg4, MultipleTypeSearchActivity.class);
        //        v0.putExtra("search_word", arg5);
        //        v0.putExtra("type", "mv1080");
        //        arg4.startActivity(v0);
        //    }
        //
        //    public void searchSongsAddList(Context arg3, String arg4) {
        //        Intent v0 = new Intent(arg3, SearchSongAddListActivity.class);
        //        v0.putExtra("ColumnName", arg4);
        //        arg3.startActivity(v0);
        //    }
        //
        //    public void searchTicket(Context arg3, String arg4) {
        //        Intent v0 = new Intent(arg3, TicketSearchActivity.class);
        //        v0.putExtra("search_word", arg4);
        //        arg3.startActivity(v0);
        //    }
        //
        //    public void searchVideoTone(Context arg4, String arg5) {
        //        Intent v0 = new Intent(arg4, MultipleTypeSearchActivity.class);
        //        v0.putExtra("search_word", arg5);
        //        v0.putExtra("type", "video_tone");
        //        arg4.startActivity(v0);
        //    }
        //
        //    public void unRegister() {
        //        if(UnionSearch.mOnSearchEventListener != null) {
        //            UnionSearch.mOnSearchEventListener = null;
        //        }
        //    }
    }
}