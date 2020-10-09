package com.calendar.loyalfans.activities


import android.Manifest.permission
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.lifecycle.MutableLiveData
import com.calendar.loyalfans.BuildConfig
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.SPHelper
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
    var spHelper: SPHelper = SPHelper(this)

    public interface OnImageSelection {
        fun onSuccess(bitmap: Bitmap?, imagePath: String?, imageURI: Uri?)
    }

    private var onImageSelection: OnImageSelection? = null

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
            imei = Common.getImei(this)
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
        val checkReplyOrNormalComment: MutableLiveData<String> =
            MutableLiveData<String>()
        val currentBalance: MutableLiveData<String> =
            MutableLiveData<String>()
        private lateinit var activity: AppCompatActivity
        fun getActivity(): AppCompatActivity {
            return activity
        }

        fun setActivity(cntActivity: AppCompatActivity) {
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
            "Choose Image",
            "Choose Video",
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
                    chooseVideoFromGallery()
                }
                2 -> {
                    takePhotoFromCamera()
                }
                3 -> {
                    takeVideoFromCamera()
                }
            }
        }
        pictureDialog.show()
    }

    open fun imageSelection() {
        val pictureDialogItems = arrayOf(
            "Choose Image",
            "Take Photo"
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

    open fun chooseVideoFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY_RESULT_CODE)
    }

    open fun takePhotoFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            pictureFile = getPictureFile()
        } catch (ex: Exception) {
            Common.showToast(this, "Photo file can't be created, please try again")
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
                    this, BuildConfig.APPLICATION_ID + ".provider",
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
            Common.showToast(this, "Video can't be created, please try again")
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
                    this, BuildConfig.APPLICATION_ID + ".provider",
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
        var contentUri: Uri? = null
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
                contentUri = data!!.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                if (contentUri != null) {
                    val cursor: Cursor? = contentResolver.query(contentUri,
                        filePathColumn, null, null, null)
                    if (cursor != null) {
                        cursor.moveToFirst()
                        val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                        path = cursor.getString(columnIndex)
                        bitmap = BitmapFactory.decodeFile(path)
                        cursor.close()
                    } else {
                        path = contentUri.path.toString()
                        bitmap = Common.getBitmapFromURI(this, contentUri)
                    }
                }
            }
        }
        if (onImageSelection != null) {
            onImageSelection!!.onSuccess(bitmap, path, contentUri)
        }
    }


}


