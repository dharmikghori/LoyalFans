package com.calendar.loyalfans.fragments.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.request.ChangePasswordRequest
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.SPHelper
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.tvToolBarName

class ChangePasswordFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.change_password)
        btnChangePassword.setOnClickListener(this)
        imgBack.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnChangePassword -> {
                    if (checkValidation()) {
                        onChangePassword()
                    }
                }
                R.id.imgBack -> {
                    activity?.onBackPressed()
                }
            }
        }
    }

    private fun checkValidation(): Boolean {
        when {
            etCurrentPassword.text.isEmpty() -> {
                activity?.let {
                    Common.showToast(it,
                        getString(R.string.current_password_validation))
                }
                return false
            }
            etNewPassword.text.isEmpty() -> {
                activity?.let {
                    Common.showToast(it,
                        getString(R.string.new_password_validation))
                }
                return false
            }
            etConfirmPassword.text.isEmpty() -> {
                activity?.let {
                    Common.showToast(it,
                        getString(R.string.confirm_password_validation))
                }
                return false
            }
            etNewPassword.text.toString() != etConfirmPassword.text.toString() -> {
                activity?.let {
                    Common.showToast(it,
                        getString(R.string.confirm_new_password_validation))
                }
                return false
            }
            etCurrentPassword.text.toString() != SPHelper(BaseActivity.getActivity()).getUserPassword() -> {
                activity?.let {
                    Common.showToast(it,
                        getString(R.string.current_password_not_match_validation))
                }
                return false
            }
        }
        return true
    }

    private fun onChangePassword() {
        val changePasswordRequest =
            ChangePasswordRequest(etCurrentPassword.text.toString(),
                etNewPassword.text.toString(), etConfirmPassword.text.toString())
        changePasswordRequest.user_id = Common.getUserId()
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.changePassword(
            changePasswordRequest, true
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        Common.showToast(BaseActivity.getActivity(), it.msg)
                        activity?.let { it1 -> Common.moveToHomeFragment(it1) }
                    }
                })
    }


}