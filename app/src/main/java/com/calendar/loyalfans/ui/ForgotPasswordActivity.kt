package com.calendar.loyalfans.ui

import android.os.Bundle
import android.view.View
import com.calendar.loyalfans.R


class ForgotPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
    }

    fun onForgotPassword(view: View) {

    }

    fun onLogin(view: View) {
        finish()
    }


}