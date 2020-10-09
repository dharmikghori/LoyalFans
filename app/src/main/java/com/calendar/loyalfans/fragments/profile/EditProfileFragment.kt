package com.calendar.loyalfans.fragments.profile

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.activities.MainActivity
import com.calendar.loyalfans.model.request.ProfileDetailRequest
import com.calendar.loyalfans.model.request.UpdateProfileRequest
import com.calendar.loyalfans.model.response.ProfileData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*

class EditProfileFragment : Fragment(), View.OnClickListener {
    var userName = ""

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
        btnUploadProfileImg.setOnClickListener(this)
        btnUploadCoverImg.setOnClickListener(this)
        getEditProfileData()
    }

    private fun getEditProfileData() {
        val postDetailRequest = ProfileDetailRequest(Common.getUserId())
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.getEditProfile(
            postDetailRequest, true
        ).observe(viewLifecycleOwner, {
            if (it.status) {
                setUpProfileData(it.data)
            }
        })

    }

    private fun setUpProfileData(data: ProfileData) {
        userName = data.username
        etUserName.setText(data.username)
        etDisplayName.setText(data.display_name)
        etAbout.setText(data.about)
        etLocation.setText(data.location)
        etWebsiteURL.setText(data.website)
        activity?.let { Common.loadImageUsingURL(imgEditProfile, data.profile_img, it, true) }
        activity?.let { Common.loadImageUsingURL(imgCoverProfile, data.cover_img, it, true) }
    }

    var profilePicBase64 = ""
    var coverPicBase64 = ""
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnUpdateProfile -> {
                    onUpdateProfile()
                }
                R.id.imgBack -> {
                    activity?.onBackPressed()
                }
                R.id.btnUploadProfileImg -> {
                    if (activity is MainActivity) {
                        val mainActivity = activity as MainActivity
                        mainActivity.imageSelection()
                        mainActivity.setOnImageSelection(object : BaseActivity.OnImageSelection {
                            override fun onSuccess(
                                bitmap: Bitmap?,
                                imagePath: String?,
                                imageUri: Uri?,
                            ) {
                                imgEditProfile.setImageBitmap(bitmap)
                                imagePath?.let {
                                    profilePicBase64 =
                                        Common.getBase64FromBitmap(it)
                                    profilePicBase64
                                }
                            }
                        })
                    }
                }
                R.id.btnUploadCoverImg,
                -> {
                    if (activity is MainActivity) {
                        val mainActivity = activity as MainActivity
                        mainActivity.imageSelection()
                        mainActivity.setOnImageSelection(object : BaseActivity.OnImageSelection {
                            override fun onSuccess(
                                bitmap: Bitmap?,
                                imagePath: String?,
                                imageUri: Uri?,
                            ) {
//                                if (bitmap != null && activity != null) {
//                                    Common.loadImageUsingBitmap(imgCoverProfile, bitmap,
//                                        activity as MainActivity)
//                                }
                                Handler().postDelayed({
                                    imgCoverProfile.setImageBitmap(bitmap)
                                }, 1000)
                                imagePath?.let {
                                    coverPicBase64 =
                                        Common.getBase64FromBitmap(it)
                                    profilePicBase64
                                }
                            }
                        })
                    }
                }
            }
        }
    }

    private fun onUpdateProfile() {
        if (checkValidation()) {
            val updateProfileRequest = UpdateProfileRequest()
            updateProfileRequest.display_name = etDisplayName.text.toString()
            updateProfileRequest.username =
                when (etUserName.text.toString()) {
                    "" -> {
                        userName
                    }
                    else -> {
                        etUserName.text.toString()
                    }
                }
            updateProfileRequest.about = etAbout.text.toString()
            updateProfileRequest.location = etLocation.text.toString()
            updateProfileRequest.website = etWebsiteURL.text.toString()
            updateProfileRequest.profile_img = profilePicBase64
            updateProfileRequest.banner_img = coverPicBase64
            val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
            baseViewModel.updateProfile(
                updateProfileRequest, true
            ).observe(viewLifecycleOwner, {
                if (it.status) {
                    if (activity is MainActivity) {
                        Common.showToast(activity as MainActivity, it.msg)
                        (activity as MainActivity).onBackPressed()
                    }
                }
            })

        }

    }

    private fun checkValidation(): Boolean {
        when {
            etUserName.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.username_Validation)) }
                return false
            }
            etDisplayName.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.display_text_Validation)) }
                return false
            }
        }
        return true
    }


}