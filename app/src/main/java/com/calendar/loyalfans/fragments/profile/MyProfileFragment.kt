package com.calendar.loyalfans.fragments.profile

import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.MainActivity
import com.calendar.loyalfans.activities.OtherProfileActivity
import com.calendar.loyalfans.model.request.ProfileDetailRequest
import com.calendar.loyalfans.model.response.ProfileData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.SPHelper
import com.calendar.loyalfans.viewpager.ProfileTabPagerAdapter
import kotlinx.android.synthetic.main.fragment_myprofile.*
import kotlinx.android.synthetic.main.layout_profile_top_view.*


class MyProfileFragment(private val profileId: String) : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = MyProfileFragment(Common.getUserId())
        fun newInstance(profileId: String) = MyProfileFragment(profileId)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_myprofile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getProfile()
        setTabLayoutAdapter()
        layFans.setOnClickListener(this)
        layFollowing.setOnClickListener(this)
        layFavorites.setOnClickListener(this)
        imgEditProfile.setOnClickListener(this)
        setViewPagerHeight()
    }

    private fun setViewPagerHeight() {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val totalHeight = displayMetrics.heightPixels
        val topViewHeight = layProfileTop.measuredHeight
        val tabLayoutHeight = tabLayout.measuredHeight
        val heightForViewPager = totalHeight - (topViewHeight + tabLayoutHeight)
        val layoutParams = viewPager.layoutParams
        layoutParams.height = heightForViewPager
        viewPager.layoutParams = layoutParams

    }

    override fun onResume() {
        super.onResume()
        getProfile()
    }

    private fun getProfile() {
        val postDetailRequest = ProfileDetailRequest(profileId)
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.getProfile(
            postDetailRequest, true
        ).observe(viewLifecycleOwner, { it ->
            if (it.status) {
                setUpProfileData(it.data)
            }
        })

    }

    private fun setUpProfileData(data: ProfileData) {
        activity?.let { SPHelper(it).saveProfileData(data) }
        tvProfileName.text = data.display_name
        tvUserName.text = data.username
        tvAbout.text = data.about
        tvTotalFansCount.text = data.fans
        tvFollowingCount.text = data.followers
        tvFavouritesCount.text = data.favorites
        activity?.let { Common.loadImageUsingURL(imgProfilePic, data.profile_img, it) }
    }

    private fun setTabLayoutAdapter() {
        val supportFragmentManager = activity?.supportFragmentManager
        if (supportFragmentManager != null && activity != null) {
            val tabsPagerAdapter =
                ProfileTabPagerAdapter(requireActivity(), supportFragmentManager, profileId)
            viewPager.offscreenPageLimit = 3
            viewPager.adapter = tabsPagerAdapter
            viewPager.isSaveFromParentEnabled = true
            tabLayout.setupWithViewPager(viewPager)
            for (i in 0 until tabLayout.tabCount) {
                val tabViewAt = tabLayout.getTabAt(i)?.view
                var tabChildCount = tabViewAt?.childCount
                if (tabChildCount == null)
                    tabChildCount = 0
                for (j in 0 until tabChildCount) {
                    if (tabViewAt != null) {
                        val tabViewChild = tabViewAt.getChildAt(j)
                        if (tabViewChild is AppCompatTextView) {
                            tabViewChild.typeface = Typeface.createFromAsset(
                                requireContext().assets,
                                "cambria.ttf"
                            )
                        }
                    }
                }

            }
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.layFans -> {
                    if (activity is MainActivity) {
                        val mainActivity = activity as MainActivity
                        mainActivity.loadFragment(6)
                    } else if (activity is OtherProfileActivity) {
                        val otherProfileActivity = activity as OtherProfileActivity
                        otherProfileActivity.loadFragment(6, otherProfileActivity.otherProfileId)
                    }
                }
                R.id.layFollowing -> {
                    if (activity is MainActivity) {
                        val mainActivity = activity as MainActivity
                        mainActivity.loadFragment(7)
                    } else if (activity is OtherProfileActivity) {
                        val otherProfileActivity = activity as OtherProfileActivity
                        otherProfileActivity.loadFragment(7, otherProfileActivity.otherProfileId)
                    }
                }
                R.id.layFavorites -> {
                    if (activity is MainActivity) {
                        val mainActivity = activity as MainActivity
                        mainActivity.loadFragment(8)
                    } else if (activity is OtherProfileActivity) {
                        val otherProfileActivity = activity as OtherProfileActivity
                        otherProfileActivity.loadFragment(8, otherProfileActivity.otherProfileId)
                    }
                }
                R.id.imgEditProfile -> {
                    if (activity is MainActivity) {
                        val mainActivity = activity as MainActivity
                        mainActivity.loadFragment(10)
                    } else if (activity is OtherProfileActivity) {
                        val otherProfileActivity = activity as OtherProfileActivity
                        otherProfileActivity.loadFragment(10, otherProfileActivity.otherProfileId)
                    }
                }
                else -> {
                }
            }
        }
    }
}