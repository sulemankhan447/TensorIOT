package com.tensoriot.utils

import android.net.Uri
import android.text.TextUtils
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide

object ImageUtils {

    fun loadRemoteImage(imageView: AppCompatImageView, url: String?) {
        imageView?.run {
            if (!TextUtils.isEmpty(url)) {
                Glide.with(this.context).load(url).into(this)
            }
        }

    }

    fun loadLocalImage(imageView: AppCompatImageView, url: Uri?) {
        imageView?.run {
            Glide.with(this.context).load(url).into(this)
        }

    }

    fun getNameForProfile():String {
        return "Profile_"+ System.currentTimeMillis()
    }
}