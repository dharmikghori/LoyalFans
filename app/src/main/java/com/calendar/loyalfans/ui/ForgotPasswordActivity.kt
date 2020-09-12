package com.calendar.loyalfans.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.request.ForgotPasswordRequest
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.retrofit.BaseViewModel
import kotlinx.android.synthetic.main.activity_forgot_password.*


class ForgotPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
    }

    fun onForgotPassword(view: View) {
        if (checkValidation()) {
            val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
            val forgotPasswordRequest = ForgotPasswordRequest(etEmail.text.toString())
            baseViewModel.forgotPassword(
                forgotPasswordRequest, true
            )
                .observe(this, {
                    com.calendar.loyalfans.utils.Common.showToast(this, it.msg)
                    if (it.status) {
                        finish()
                    }
                })
        }
    }

    private fun checkValidation(): Boolean {
        if (etEmail.text.isEmpty() || !Common.checkEmail(etEmail.text.toString())) {
            Common.showToast(this, getString(R.string.valid_email))
            return false
        }
        return true
    }

    fun onLogin(view: View) {
        finish()
    }


}