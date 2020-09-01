package com.calendar.loyalfans.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
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
//            if (spHelper.getLoginData() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
//            } else {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
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
        grantResults: IntArray
    ) {
        when (requestCode) {

            PERMISSIONS_REQUEST_WRITE_STORAGE, PERMISSIONS_REQUEST_READ_STORAGE, PERMISSIONS_REQUEST_CAMERA -> {
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

    }

}