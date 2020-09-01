package com.calendar.loyalfans.fragments.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
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
                    onChangePassword()
                }
                R.id.imgBack -> {
                    activity?.onBackPressed()
                }
            }
        }
    }

    private fun onChangePassword() {

    }


}