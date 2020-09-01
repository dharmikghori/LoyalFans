package com.calendar.loyalfans.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.calendar.loyalfans.R


class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLogin(view: View) {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    fun onRegister(view: View) {
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }

    fun onForgotPassword(view: View) {
        startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
    }


}