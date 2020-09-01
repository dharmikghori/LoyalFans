package com.calendar.loyalfans.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.calendar.loyalfans.R


class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun onLogin(view: View) {
        onBackPressed()
    }

    fun onRegister(view: View) {
        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
        finish()
    }


}