package com.ayush.talkio.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

object Compressor {

    fun compress(uri: Uri, ctx: Context): ByteArray {
        val bmp: Bitmap = MediaStore.Images.Media.getBitmap(
            ctx.contentResolver,
            uri
        )

        val byteStream = ByteArrayOutputStream()

        bmp.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESSION_QUALITY, byteStream)

        return byteStream.toByteArray()
    }


}