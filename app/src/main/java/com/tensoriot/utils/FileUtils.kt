package com.tensoriot.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.tensoriot.R
import java.io.File

object FileUtils {
    fun createImageUri(context: Context): Uri? {
        val image = File(context.applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(
            context.applicationContext, context.getString(R.string.authority), image
        )
    }

}