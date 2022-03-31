package com.km.common.resultContract

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

/**
 * 调起系统裁剪图片页面
 */
class CropPictureContract : ActivityResultContract<CropPictureParameter, Uri?>() {
    private lateinit var outputUri: Uri

    override fun createIntent(context: Context, input: CropPictureParameter) =
        Intent("com.android.camera.action.CROP")
            .apply {
                outputUri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    input.outputContentValues
                )!!
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setDataAndType(input.inputUri, "image/*")
                putExtra("crop", "true")
                putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
                putExtra("aspectX", input.aspectX)
                putExtra("aspectY", input.aspectY)
                putExtra("outputX", input.outputX)
                putExtra("outputY", input.outputY)
                putExtra("scale", true)
                putExtra("scaleUpIfNeeded", true)
                putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
                putExtra("return-data", false)
            }

    override fun parseResult(resultCode: Int, result: Intent?): Uri? =
        if (resultCode == Activity.RESULT_OK) outputUri else null
}

data class CropPictureParameter(
    val inputUri: Uri,
    val aspectX: Int,
    val aspectY: Int,
    var outputX: Int = aspectX,
    var outputY: Int = aspectY,
    var outputContentValues: ContentValues = ContentValues()
)