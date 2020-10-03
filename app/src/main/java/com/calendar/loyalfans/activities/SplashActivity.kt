package com.calendar.loyalfans.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common
import com.facebook.FacebookSdk
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        FacebookSdk.sdkInitialize(this)
        checkAllPermissionsAreAllow()

    }

    private fun checkAllPermissionsAreAllow() {
        val isAllPermissionGranted = checkSelfPermissions()
        if (isAllPermissionGranted) {
            moveToNext()
        }
    }


    private lateinit var postDelayed: Handler
    private lateinit var runnable: Runnable

    private fun moveToNext() {
        postDelayed = Handler(Looper.getMainLooper())
        runnable = Runnable {
            if (spHelper.getLoginData() == null) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        postDelayed.postDelayed(runnable, 1000)
    }


    private fun checkSelfPermissions(): Boolean {
        return checkSelfPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            PERMISSIONS_REQUEST_READ_STORAGE
        ) && checkSelfPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            PERMISSIONS_REQUEST_WRITE_STORAGE
        ) && checkSelfPermission(
            Manifest.permission.CAMERA,
            PERMISSIONS_REQUEST_CAMERA
        ) && checkSelfPermission(
            Manifest.permission.READ_PHONE_STATE,
            PERMISSIONS_REQUEST_PHONE_STATE
        )
    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {

        if (ContextCompat.checkSelfPermission(
                this@SplashActivity,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(permission),
                requestCode
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {

            PERMISSIONS_REQUEST_WRITE_STORAGE, PERMISSIONS_REQUEST_READ_STORAGE, PERMISSIONS_REQUEST_CAMERA, PERMISSIONS_REQUEST_PHONE_STATE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    checkAllPermissionsAreAllow()
                } else {
                    permissionDeniedMessage()
                }
            }
        }


    }

    private fun permissionDeniedMessage() {
        Common.showToast(
            this@SplashActivity,
            getString(R.string.permission_error)
        )
    }

    companion object {
        private const val PERMISSIONS_REQUEST_READ_STORAGE = 100
        private const val PERMISSIONS_REQUEST_WRITE_STORAGE = 101
        private const val PERMISSIONS_REQUEST_CAMERA = 102
        private const val PERMISSIONS_REQUEST_PHONE_STATE = 103

    }


    private fun printKeyHash(context: Activity): String? {
        val packageInfo: PackageInfo
        var key: String? = null
        val any = try {
            //getting application package name, as defined in manifest
            val packageName: String = context.applicationContext.packageName

            //Retriving package info
            packageInfo = context.packageManager.getPackageInfo(packageName,
                PackageManager.GET_SIGNATURES)
            Log.e("Package Name=", context.applicationContext.packageName)
            for (signature in packageInfo.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                key = String(Base64.encode(md.digest(), 0))

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("Name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("No such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }
        return key
    }
}