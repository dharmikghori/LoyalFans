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
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Base64
import android.view.Window
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.activities.LoginActivity
import com.calendar.loyalfans.activities.MainActivity
import com.calendar.loyalfans.fragments.home.HomeFragment
import com.calendar.loyalfans.fragments.notifications.NotificationFragment
import com.calendar.loyalfans.fragments.password.ChangePasswordFragment
import com.calendar.loyalfans.fragments.payment.*
import com.calendar.loyalfans.fragments.post.AddPostFragment
import com.calendar.loyalfans.fragments.post.CommentsFragment
import com.calendar.loyalfans.fragments.post.PostDetailsFragment
import com.calendar.loyalfans.fragments.ppv.AddPpvPostFragment
import com.calendar.loyalfans.fragments.ppv.PPVFragment
import com.calendar.loyalfans.fragments.profile.*
import com.calendar.loyalfans.fragments.searchFragment.SearchFragment
import com.calendar.loyalfans.fragments.setting.HelpAndSupportFragment
import com.calendar.loyalfans.fragments.setting.NotificationSettingFragment
import com.calendar.loyalfans.fragments.setting.SecuritySettingFragment
import com.calendar.loyalfans.model.request.SendTipRequest
import com.calendar.loyalfans.model.request.UpdateFCMToken
import com.calendar.loyalfans.model.response.PostData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.facebook.login.LoginManager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class Common {
    companion object {


        fun showSendDialog(context: Context, postData: PostData) {
            val dialog = Dialog(context, R.style.FullScreenDialogStyle)
            dialog.setContentView(R.layout.layout_send_tip_dialog)
            dialog.show()
            val etTipAmount = dialog.findViewById<EditText>(R.id.etTipAmount)
            dialog.findViewById<Button>(R.id.btnSendTipDialog).setOnClickListener {
                if (etTipAmount.length() > 0) {
                    val sendTipRequest =
                        SendTipRequest(postData.user_id, etTipAmount.text.toString(), postData.id)
                    val baseViewModel =
                        ViewModelProvider(BaseActivity.getActivity()).get(BaseViewModel::class.java)
                    baseViewModel.sendTip(
                        sendTipRequest, true
                    ).observe(BaseActivity.getActivity(), {
                        if (it.status) {
                            showToast(context, it.msg)
                            dialog.dismiss()
                        }
                    })
                } else {
                    showToast(context, context.getString(R.string.enter_tip_amount_validation))

                }
            }
        }

        fun withdrawalRequestDialog(context: Context) {
            val dialog = Dialog(context, R.style.FullScreenDialogStyle)
            dialog.setContentView(R.layout.layout_send_tip_dialog)
            dialog.show()
            dialog.findViewById<TextView>(R.id.tvTitle).text =
                context.getString(R.string.send_request_withdrawal)
            val etTipAmount = dialog.findViewById<EditText>(R.id.etTipAmount)
            val btnSendTipDialog = dialog.findViewById<Button>(R.id.btnSendTipDialog)
            etTipAmount.hint = context.getString(R.string.enter_amount)
            btnSendTipDialog.text = context.getString(R.string.send_request)
            btnSendTipDialog.setOnClickListener {
                if (etTipAmount.length() > 0) {
                    val sendTipRequest =
                        SendTipRequest("", etTipAmount.text.toString(), "")
                    val baseViewModel =
                        ViewModelProvider(BaseActivity.getActivity()).get(BaseViewModel::class.java)
                    baseViewModel.withdrawalRequest(
                        sendTipRequest, true
                    ).observe(BaseActivity.getActivity(), {
                        if (it.status) {
                            showToast(context, it.msg)
                            dialog.dismiss()
                        }
                    })
                } else {
                    showToast(context, context.getString(R.string.enter_withdraw_amount_validation))

                }
            }
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
                    "SubscriptionPlan"
                }
                15 -> {
                    "PPVFragment"
                }
                16 -> {
                    "AddPpvPostFragment"
                }
                17 -> {
                    "CommentFragment"
                }
                18 -> {
                    "BankFragment"
                }
                19 -> {
                    "BankTransferAndW9Fragment"
                }
                20 -> {
                    "W9Fragment"
                }
                21 -> {
                    "BankTransferFragment"
                }
                22 -> {
                    "HelpAndSupportFragment"
                }
                23 -> {
                    "PaymentHistoryFragment"
                }
                24 -> {
                    "StatementsFragment"
                }
                25 -> {
                    "OtherMessageFragment"
                }
                26 -> {
                    "MessageAnalyticFragment"
                }
                27 -> {
                    "MessageAnalyticFragment"
                }
                else -> ""
            }
        }

        fun getFragmentBasedOnType(type: Int): Fragment? {
            return when (type) {
                1 -> {
                    HomeFragment.newInstance()
                }
                2 -> {
                    SearchFragment.newInstance()
                }
                3 -> {
                    AddPostFragment.newInstance()
                }
                4 -> {
                    NotificationFragment.newInstance()
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
                    FavoriteFragment.newInstance()
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
                14 -> {
                    SubscriptionFragment.newInstance()
                }
                15 -> {
                    PPVFragment.newInstance()
                }
                16 -> {
                    AddPpvPostFragment.newInstance()
                }
                18 -> {
                    BankFragment.newInstance()
                }
                20 -> {
                    BankW9Fragment.newInstance()
                }
                21 -> {
                    BankTransferFragment.newInstance()
                }
                22 -> {
                    HelpAndSupportFragment.newInstance()
                }
                23 -> {
                    PaymentHistoryFragment.newInstance()
                }
                24 -> {
                    StatementFragment.newInstance()
                }
                27 -> {
                    PostDetailsFragment.newInstance()
                }
                else -> null
            }
        }

        fun setUpTablayOutStyle(tabLayout: TabLayout) {
            for (i in 0 until tabLayout.tabCount) {
                val tabViewAt = tabLayout.getTabAt(i)?.view
                var tabChildCount = tabViewAt?.childCount
                if (tabChildCount == null)
                    tabChildCount = 0
                for (j in 0 until tabChildCount) {
                    if (tabViewAt != null) {
                        val tabViewChild = tabViewAt.getChildAt(j)
                        if (tabViewChild is AppCompatTextView) {
                            tabViewChild.typeface = Typeface.createFromAsset(
                                BaseActivity.getActivity().assets,
                                "cambria.ttf"
                            )
                        }
                    }
                }

            }

        }

        fun getFragmentBasedOnType(type: Int, profileId: String): Fragment? {
            return when (type) {
                5 -> {
                    MyProfileFragment.newInstance(profileId)
                }
                6 -> {
                    FansFragment.newInstance(profileId)
                }
                7 -> {
                    FollowingFragment.newInstance(profileId)
                }
                8 -> {
                    FavoriteFragment.newInstance(profileId)
                }
                17 -> {
                    CommentsFragment.newInstance(profileId)
                }
                else -> null
            }
        }

        fun manageBottomVisibilitiesAndSelectionBasedOnType(
            type: Int,
            imgHome: ImageView,
            imgSearch: ImageView,
            imgNotification: ImageView,
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
                if (message.toLowerCase() != "no data found") {
                    if (isLongMessage) {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
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
            var context1 = context as Activity
            if (context1.isFinishing) {
                context1 = BaseActivity.getActivity()
            }
            try {
                if (progressDialog != null && progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                }
                progressDialog = Dialog(context1)
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
//                if (isCache) {
                Glide.with(context)
                    .load(url)
                    .fitCenter()
                    .placeholder(R.drawable.place_holder)
                    .into(imageView)
//                } else {
//                    Glide.with(context)
//                        .load(url)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true)
//                        .fitCenter()
//                        .placeholder(R.drawable.place_holder)
//                        .into(imageView)
//                }
            }
        }

        fun setupVerticalRecyclerView(recyclerView: RecyclerView, activity: Context?) {
            recyclerView.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        fun getBase64FromBitmap(path: String): String {
            val base64: String?
            val file = File(path)
            val buffer = ByteArray(file.length().toInt() + 100)
            lateinit var inputStream: FileInputStream
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val parcelFileDescriptor =
                    BaseActivity.getActivity().contentResolver.openFileDescriptor(Uri.fromFile(file),
                        "r",
                        null)

                if (parcelFileDescriptor != null) {
                    inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                }
            } else {
                inputStream = FileInputStream(file)
            }
            val length = inputStream.read(buffer)
            base64 = Base64.encodeToString(buffer, 0, length,
                Base64.DEFAULT)
            return base64
        }

        private var serverFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        private var serverFullDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
        var localDateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.US)
        var localTimeFormat = SimpleDateFormat("hh:mm a", Locale.US)

        fun formatDate(strDate: String): String {
            serverFormat.timeZone = TimeZone.getTimeZone("UTC")
            val format = serverFormat.parse(strDate)
            if (format != null) {
                localDateFormat.timeZone = TimeZone.getDefault();
                return localDateFormat.format(format)
            }
            return ""
        }

        fun formatTime(strDate: String): String {
            serverFullDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val format = serverFullDateFormat.parse(strDate)
            if (format != null) {
                localTimeFormat.timeZone = TimeZone.getDefault();
                return localTimeFormat.format(format)
            }
            return ""
        }

        fun updateTokenToServer(token: String) {
            if (SPHelper(BaseActivity.getActivity()).getLoginData() == null) {
                BaseActivity.firebaseToken = ""
                return
            }
            val updateTokenRequest = UpdateFCMToken(token)
            val baseViewModel =
                ViewModelProvider(BaseActivity.getActivity()).get(BaseViewModel::class.java)
            baseViewModel.updateFcmToken(
                updateTokenRequest, false
            )
                .observe(BaseActivity.getActivity(), {})
        }
    }
}