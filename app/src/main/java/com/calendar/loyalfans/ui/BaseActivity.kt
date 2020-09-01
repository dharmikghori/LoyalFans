package com.calendar.loyalfans.ui


import android.Manifest.permission
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.calendar.loyalfans.BuildConfig
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import java.io.File
import java.util.*

open class BaseActivity : AppCompatActivity() {

    private val permissions = arrayOf(permission.WRITE_EXTERNAL_STORAGE, permission.CAMERA)
    private var photoURI: Uri? = null
    private var videoURI: Uri? = null
    private var pictureFile: File? = null
    private var videoFile: File? = null
    private var PERMISSION_RESULT_CODE = 200
    private var GALLERY_RESULT_CODE = 1
    private var IMAGE_CAPTURE_RESULT_CODE = 2
    private var VIDEO_CAPTURE_RESULT_CODE = 4

    public interface OnImageSelection {
        fun onSuccess(bitmap: Bitmap?, imagePath: String?)
    }

    private var onImageSelection: OnImageSelection? = null

    @JvmName("setOnImageSelection1")
    public fun setOnImageSelection(onImageSelection: OnImageSelection) {
        this.onImageSelection = onImageSelection
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setActivity(this)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        hideActionBar()

        if (imei == "") {
            imei = Common.getIMEI(this)
        }

        if (firebaseToken == "") {
            generateToken()
        }


    }

    private fun generateToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                firebaseToken = task.result?.token.toString()
            })
    }

    open fun hideActionBar() {
        if (supportActionBar != null) supportActionBar!!.hide()
    }

    companion object {
        var firebaseToken = ""
        var imei = ""
        private lateinit var activity: Activity
        fun getActivity(): Activity {
            return activity
        }

        fun setActivity(cntActivity: Activity) {
            activity = cntActivity
        }

    }

    open fun checkAndRequestPermission(): Boolean {
        val pendingPermission = arrayOfNulls<String>(permissions.size)
        var isPermissionGranted = true
        for (i in permissions.indices) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    permissions[i]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                pendingPermission[i] = permissions[i]
                isPermissionGranted = false
            }
        }
        if (!isPermissionGranted) {
            ActivityCompat.requestPermissions(
                this,
                pendingPermission,
                PERMISSION_RESULT_CODE
            )
        }
        return isPermissionGranted
    }

    open fun imageAndVideoSelectionForPost() {
        val pictureDialogItems = arrayOf(
            "Gallery",
            "Take Photo",
            "Take Video"
        )
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(getString(R.string.media_selection))
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog: DialogInterface, which: Int ->
            when (which) {
                0 -> {
                    choosePhotoFromGallery()
                }
                1 -> {
                    takePhotoFromCamera()
                }
                2 -> {
                    takeVideoFromCamera()
                }
            }
        }
        pictureDialog.show()
    }


    open fun choosePhotoFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY_RESULT_CODE)
    }

    open fun takePhotoFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            pictureFile = getPictureFile()
        } catch (ex: Exception) {
            Toast.makeText(
                this,
                "Photo file can't be created, please try again",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                photoURI = Uri.fromFile(pictureFile)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 0.5)
                startActivityForResult(cameraIntent, IMAGE_CAPTURE_RESULT_CODE)
            } else {
                photoURI = FileProvider.getUriForFile(
                    this, BuildConfig.APPLICATION_ID.toString() + ".provider",
                    pictureFile!!
                )
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 0.5)
                startActivityForResult(cameraIntent, IMAGE_CAPTURE_RESULT_CODE)
            }
        } catch (e: Exception) {
        }
    }

    open fun takeVideoFromCamera() {
        val videoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        try {
            videoFile = getVideoFile()
        } catch (ex: Exception) {
            Toast.makeText(
                this,
                "Video file can't be created, please try again",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                videoURI = Uri.fromFile(videoFile)
                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI)
                videoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 0.5)
                startActivityForResult(videoIntent, VIDEO_CAPTURE_RESULT_CODE)
            } else {
                videoURI = FileProvider.getUriForFile(
                    this, BuildConfig.APPLICATION_ID.toString() + ".provider",
                    videoFile!!
                )
                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI)
                videoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 0.5)
                startActivityForResult(videoIntent, VIDEO_CAPTURE_RESULT_CODE)
            }
        } catch (e: Exception) {
        }
    }

    open fun getVideoFile(): File? {
        return File(externalCacheDir.toString() + "/${Calendar.getInstance().timeInMillis}.mp4")
    }

    open fun getPictureFile(): File? {
        return File(externalCacheDir.toString() + "/${Calendar.getInstance().timeInMillis}.jpg")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) {
            return
        }
        var path = ""
        var bitmap: Bitmap? = null
        if (requestCode == IMAGE_CAPTURE_RESULT_CODE) {
            if (RESULT_OK == resultCode) {
                path = pictureFile!!.absolutePath
                bitmap = Common.getBitmapFromImagePath(this, path)
            }
        } else if (requestCode == VIDEO_CAPTURE_RESULT_CODE) {
            if (RESULT_OK == resultCode) {
                path = videoFile!!.absolutePath
                bitmap = Common.getBitmapFromImagePath(this, path)
            }
        } else if (GALLERY_RESULT_CODE == requestCode) {
            if (RESULT_OK == resultCode) {
                val contentURI = data!!.data
                if (contentURI != null) {
                    path = contentURI.path.toString()
                    bitmap = Common.getBitmapFromURI(this, contentURI)
                }
            }
        }
        if (onImageSelection != null) {
            onImageSelection!!.onSuccess(bitmap, path)
        }
    }


}


