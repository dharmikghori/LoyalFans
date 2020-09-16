package com.calendar.loyalfans.utils

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaMetadataRetriever
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Base64
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
import com.calendar.loyalfans.ui.LoginActivity
import com.calendar.loyalfans.ui.MainActivity
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import java.io.*
import java.util.regex.Pattern


class Common {
    companion object {

        private val homeFragment = HomeFragment.newInstance()
        private val searchFragment = SearchFragment.newInstance()
        private val addPostFragment = AddPostFragment.newInstance()

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
                14 -> {
                    "EditPostFragment"
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
                    MyProfileFragment.newInstance()
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
            BaseActivity.getActivity().runOnUiThread {
                if (isLongMessage) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun getImei(baseActivity: BaseActivity): String {
            try {
                val telephonyManager =
                    baseActivity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val iMEI: String?
                if (ContextCompat.checkSelfPermission(
                        baseActivity,
                        Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    iMEI = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        telephonyManager.imei
                    } else {
                        telephonyManager.deviceId
                    }
                    return if (iMEI != null && iMEI.isNotEmpty()) {
                        iMEI
                    } else {
                        Build.SERIAL
                    }
                }
            } catch (e: Exception) {
                val errors = StringWriter()
                e.printStackTrace(PrintWriter(errors))
                return Settings.Secure.getString(baseActivity.contentResolver,
                    Settings.Secure.ANDROID_ID)
            }
            return baseActivity.getString(R.string.not_found)
        }

        private val SupportedImageExt = arrayOf("jpeg", "png", "jpg")

        fun isVideo(fileName: String): Boolean {
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
                if (progressDialog != null && progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                }
            } catch (ex: java.lang.Exception) {
            }
        }


        fun getBitmapFromImagePath(activity: Context, imagePath: String): Bitmap? {
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

        fun isOnline(context: Context, isShowMessage: Boolean): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            val isOnline = networkInfo != null && networkInfo.isConnected
            if (!isOnline && isShowMessage) {
                showToast(context, context.getString(R.string.internet_error))
            }
            return isOnline
        }

        fun checkEmail(email: String): Boolean {
            return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email).matches()
        }

        fun automaticallyLogoutOnUnauthorizedOrForbidden() {
            val context = BaseActivity.getActivity()
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            LoginManager.getInstance().logOut()

            SPHelper(context).clearLoginSession()
            val i = Intent(context, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(i)
        }

        fun getUserId(): String {
            val loginData = SPHelper(BaseActivity.getActivity()).getLoginData()
            if (loginData?.data != null) {
                return loginData.data.user_id
            }
            return ""
        }

        fun moveToHomeFragment(activity: FragmentActivity) {
            if (activity is MainActivity) {
                activity.loadFragment(1)
            }
        }

        fun loadImageUsingURL(
            imageView: ImageView,
            url: String?,
            context: Context,
            isCache: Boolean = false,
        ) {
            if (url != null) {
                if (isCache) {
                    Glide.with(context)
                        .load(url)
                        .fitCenter()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(imageView)
                } else {
                    Glide.with(context)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .fitCenter()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(imageView)
                }
            }
        }

        fun setupVerticalRecyclerView(recyclerView: RecyclerView, activity: Context?) {
            recyclerView.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        @Throws(Throwable::class)
        fun retriveVideoFrameFromVideo(videoPath: String?): Bitmap? {
            val bitmap: Bitmap?
            var mediaMetadataRetriever: MediaMetadataRetriever? = null
            try {
                mediaMetadataRetriever = MediaMetadataRetriever()
                mediaMetadataRetriever.setDataSource(videoPath, HashMap<String, String>())
                //   mediaMetadataRetriever.setDataSource(videoPath);
                bitmap = mediaMetadataRetriever.frameAtTime
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)
            } finally {
                mediaMetadataRetriever?.release()
            }
            return bitmap
        }

        fun getBase64FromBitmap(path: String): String {
            var base64: String? = ""
            val file = File(path)
            val buffer = ByteArray(file.length().toInt() + 100)
            val length = FileInputStream(file).read(buffer)
            base64 = Base64.encodeToString(buffer, 0, length,
                Base64.DEFAULT)
            return base64
        }
    }
}