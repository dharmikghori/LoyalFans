package com.calendar.loyalfans.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*

class EditProfileFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = EditProfileFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.edit_profile)
        btnUpdateProfile.setOnClickListener(this)
        imgBack.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnUpdateProfile -> {
                    onUpdateProfile()
                }
                R.id.imgBack -> {
                    activity?.onBackPressed()
                }
            }
        }
    }

    private fun onUpdateProfile() {

    }


}