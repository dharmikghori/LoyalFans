package com.calendar.loyalfans.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
import com.calendar.loyalfans.fragments.home.HomeFragment
import com.calendar.loyalfans.fragments.password.ChangePasswordFragment
import com.calendar.loyalfans.fragments.payment.AddCardFragment
import com.calendar.loyalfans.fragments.post.AddPostFragment
import com.calendar.loyalfans.fragments.profile.*
import com.calendar.loyalfans.fragments.searchFragment.SearchFragment
import com.calendar.loyalfans.fragments.setting.NotificationSettingFragment
import com.calendar.loyalfans.fragments.setting.SecuritySettingFragment
import com.calendar.loyalfans.ui.BaseActivity
import java.io.File
import java.io.IOException


class Common {
    companion object {

        private val homeFragment = HomeFragment.newInstance()
        private val searchFragment = SearchFragment.newInstance()
        private val addPostFragment = AddPostFragment.newInstance()
        private val myProfileFragment = MyProfileFragment.newInstance()

        fun showSendDialog(context: Context) {
            val dialog = Dialog(context, R.style.FullScreenDialogStyle)
            dialog.setContentView(R.layout.layout_send_tip_dialog)
            dialog.show()
        }

        fun getTagBasedOnType(type: Int): String {
            return when (type) {
                1 -> {
                    "HomeFragment"
                }
                2 -> {
                    "SearchFragment"
                }
                3 -> {
                    "AddPostFragment"
                }
                4 -> {
                    "NotificationFragment"
                }
                5 -> {
                    "ProfileFragment"
                }
                6 -> {
                    "FansFragment"
                }
                7 -> {
                    "FollowingFragment"
                }
                8 -> {
                    "FavoriteFragment"
                }
                9 -> {
                    "ChangePasswordFragment"
                }
                10 -> {
                    "EditProfileFragment"
                }
                11 -> {
                    "NotificationSettingFragment"
                }
                12 -> {
                    "SecuritySettingFragment"
                }
                13 -> {
                    "AddCardFragment"
                }
                else -> ""
            }
        }

        fun getFragmentBasedOnType(type: Int): Fragment? {
            return when (type) {
                1 -> {
                    homeFragment
                }
                2 -> {
                    searchFragment
                }
                3 -> {
                    addPostFragment
                }
                4 -> {
                    null
                }
                5 -> {
                    myProfileFragment
                }
                6 -> {
                    FansFragment.newInstance()
                }
                7 -> {
                    FollowingFragment.newInstance()
                }
                8 -> {
                    FavoritesFragment.newInstance()
                }
                9 -> {
                    ChangePasswordFragment.newInstance()
                }
                10 -> {
                    EditProfileFragment.newInstance()
                }
                11 -> {
                    NotificationSettingFragment.newInstance()
                }
                12 -> {
                    SecuritySettingFragment.newInstance()
                }
                13 -> {
                    AddCardFragment.newInstance()
                }
                else -> null
            }
        }

        fun manageBottomVisibilitiesAndSelectionBasedOnType(
            type: Int,
            imgHome: ImageView,
            imgSearch: ImageView,
            imgNotification: ImageView,
            imgProfile: ImageView,
            resources: Resources,
            theme: Resources.Theme,
        ) {
            when (type) {
                1 -> {
                    imgHome.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_home_select,
                            theme
                        )
                    )
                    imgSearch.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_search_unselect,
                            theme
                        )
                    )
                    imgNotification.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_notification_unselect,
                            theme
                        )
                    )
//                    imgProfile.setImageDrawable(
//                        ResourcesCompat.getDrawable(
//                            resources,
//                            R.mipmap.ic_notification_unselect,
//                            theme
//                        )
//                    )
                }
                2 -> {
                    imgHome.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_home_unselect,
                            theme
                        )
                    )
                    imgSearch.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_search_select,
                            theme
                        )
                    )
                    imgNotification.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_notification_unselect,
                            theme
                        )
                    )
//                    imgProfile.setImageDrawable(
//                        ResourcesCompat.getDrawable(
//                            resources,
//                            R.mipmap.ic_notification_unselect,
//                            theme
//                        )
//                    )
                }
                3 -> {
                    imgHome.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_home_unselect,
                            theme
                        )
                    )
                    imgSearch.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_search_unselect,
                            theme
                        )
                    )
                    imgNotification.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_notification_unselect,
                            theme
                        )
                    )
//                    imgProfile.setImageDrawable(
//                        ResourcesCompat.getDrawable(
//                            resources,
//                            R.mipmap.ic_notification_unselect,
//                            theme
//                        )
//                    )
                }
                4 -> {
                    imgHome.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_home_unselect,
                            theme
                        )
                    )
                    imgSearch.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_search_unselect,
                            theme
                        )
                    )
                    imgNotification.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_notification_select,
                            theme
                        )
                    )
//                    imgProfile.setImageDrawable(
//                        ResourcesCompat.getDrawable(
//                            resources,
//                            R.mipmap.ic_notification_unselect,
//                            theme
//                        )
//                    )
                }
                5 -> {
                    imgHome.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_home_unselect,
                            theme
                        )
                    )
                    imgSearch.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_search_unselect,
                            theme
                        )
                    )
                    imgNotification.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.ic_notification_unselect,
                            theme
                        )
                    )
//                    imgProfile.setImageDrawable(
//                        ResourcesCompat.getDrawable(
//                            resources,
//                            R.mipmap.ic_notification_select,
//                            theme
//                        )
//                    )
                }
            }
        }

        fun showToast(context: Context, message: String, isLongMessage: Boolean = false) {
            if (isLongMessage) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        fun getIMEI(baseActivity: BaseActivity): String {
            return ""
        }

        private val SupportedImageExt = arrayOf("jpeg", "png", "jpg")

        public fun isVideo(fileName: String): Boolean {
            val extension: String = getFileExtension(fileName)
            for (photosVideoType in SupportedImageExt) {
                if (extension.equals(photosVideoType, ignoreCase = true)) {
                    return false
                }
            }
            return true
        }

        private fun getFileExtension(fileName: String): String {
            try {
                return fileName.substring(fileName.lastIndexOf(".")).replace(".", "")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        fun rotateBitmap(photoPath: String?, bitmap: Bitmap): Bitmap? {
            val ei: ExifInterface
            try {
                ei = ExifInterface(photoPath!!)
                val orientation = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
                )
                val rotatedBitmap: Bitmap
                rotatedBitmap = when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(
                        bitmap,
                        90f
                    )
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(
                        bitmap,
                        180f
                    )
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(
                        bitmap,
                        270f
                    )
                    ExifInterface.ORIENTATION_NORMAL -> bitmap
                    else -> bitmap
                }
                return rotatedBitmap
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return bitmap
        }

        private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(
                source, 0, 0, source.width, source.height,
                matrix, true
            )
        }

        private var progressDialog: Dialog? = null
        fun displayProgress(context: Context) {
            val context1 = context as Activity
            if (context1.isFinishing) {
                return
            }
            try {
                if (progressDialog != null && progressDialog!!.isShowing()) {
                    progressDialog!!.dismiss()
                }
                progressDialog = Dialog(context)
                progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                if (progressDialog!!.window != null) {
                    progressDialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
                }
                progressDialog!!.setContentView(R.layout.circular_progressbar)
                progressDialog!!.setCancelable(true)
                progressDialog!!.setCanceledOnTouchOutside(false)
                progressDialog!!.show()
            } catch (ex: java.lang.Exception) {
            }
        }

        fun dismissProgress() {
            try {
                if (progressDialog != null && progressDialog!!.isShowing()) {
                    progressDialog!!.dismiss()
                }
            } catch (ex: java.lang.Exception) {
            }
        }


        public fun getBitmapFromImagePath(activity: Context, imagePath: String): Bitmap? {
            try {
                return MediaStore.Images.Media.getBitmap(
                    activity.contentResolver, Uri.fromFile(File(imagePath))
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        fun getBitmapFromURI(activity: Context, contentUri: Uri): Bitmap? {
            try {
                return MediaStore.Images.Media.getBitmap(
                    activity.contentResolver, contentUri
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }
}