/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.echo_kt.ui.main

import android.content.ContentUris
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.echo_kt.R
import com.example.echo_kt.play.PlayerManager

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("imgPlay")
fun imgPlay(view: View,playStatus: Int){
    //改变播放键显示
    when(playStatus){
        PlayerManager.RELEASE, PlayerManager.PAUSE ->{
            view.isSelected = false
            Log.i("iiiii", "暂停")
        }
        PlayerManager.START, PlayerManager.RESUME ->{
            view.isSelected = true
            Log.i("iiiii", "播放")
        }
    }
}
@BindingAdapter("url")
fun setImgUrl(view: ImageView, url: Int) {
    Glide.with(view.context).load(url).into(view)
}

@BindingAdapter("urlAlbum")
fun setImgUrl(view: ImageView, url: Long) {
    Glide.with(view.context)
        .load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), url))
        .apply(RequestOptions.bitmapTransform(CircleCrop()))
        .error(R.mipmap.album)
        .into(view)
}
