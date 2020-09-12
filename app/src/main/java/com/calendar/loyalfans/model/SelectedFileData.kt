package com.calendar.loyalfans.model

import android.graphics.Bitmap
import android.net.Uri


open class SelectedFileData {
    var filePath: String = ""
    var base64String: String = ""
    var thumbnailPath: String = ""
    var imageBitmap: Bitmap? = null
    var imageUri: Uri? = null
}