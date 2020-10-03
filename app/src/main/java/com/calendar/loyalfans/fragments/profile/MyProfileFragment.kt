package com.calendar.loyalfans.fragments.profile

import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.MainActivity
import com.calendar.loyalfans.activities.OtherProfileActivity
import com.calendar.loyalfans.model.request.PaidSubscriptionRequest
import com.calendar.loyalfans.model.request.ProfileDetailRequest
import com.calendar.loyalfans.model.response.ProfileData
import com.calendar.loyalfans.model.response.ProfileSubscriptionData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.SPHelper
import com.calendar.loyalfans.viewpager.ProfileTabPagerAdapter
import kotlinx.android.synthetic.main.fragment_myprofile.*
import kotlinx.android.synthetic.main.layout_profile_subscription.*
import kotlinx.android.synthetic.main.layout_profile_top_view.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*


class MyProfileFragment(private val profileId: String) : Fragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

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
        if (profileId != Common.getUserId()) {
            tvToolBarName.text = getString(R.string.other_profile)
            cbFavouriteProfile.visibility = View.VISIBLE
            cbFavouriteProfile.setOnCheckedChangeListener(this)
            imgBack.visibility = View.VISIBLE
            imgBack.setOnClickListener(this)
        } else {
            cbFavouriteProfile.visibility = View.GONE
            tvToolBarName.text = getString(R.string.myprofile)
            imgBack.visibility = View.GONE
        }
        getProfile(true)
        layFans.setOnClickListener(this)
        layFollowing.setOnClickListener(this)
        layFavorites.setOnClickListener(this)
        imgEditProfile.setOnClickListener(this)
        setViewPagerHeight()
    }

    private val isFirstTime = true
    private fun setViewPagerHeight() {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val totalHeight = displayMetrics.heightPixels
        if (isFirstTime) {
            var topViewHeight = layProfileTop.measuredHeight
            var tabLayoutHeight = tabLayout.measuredHeight
            if (topViewHeight == 0) {
                topViewHeight = 150
            }
            if (tabLayoutHeight == 0) {
                tabLayoutHeight = 150
            }
            val heightForViewPager =
                totalHeight - (topViewHeight + tabLayoutHeight + 50)
            val layoutParams = viewPager.layoutParams
            layoutParams.height = heightForViewPager
            viewPager.layoutParams = layoutParams
        }
    }

    override fun onResume() {
        super.onResume()
        getProfile(false)
    }

    private lateinit var profileData: ProfileData
    private fun getProfile(isSubscriptionManage: Boolean) {
        val postDetailRequest = ProfileDetailRequest(profileId)
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.getProfile(
            postDetailRequest, true
        ).observe(viewLifecycleOwner, {
            if (it.status) {
                profileData = it.data
                setUpProfileData(profileData, isSubscriptionManage)
            }
        })

    }

    private fun setUpProfileData(data: ProfileData, isSubscriptionManage: Boolean) {
        if (isSubscriptionManage) {
            manageOtherProfile()
        }
        if (Common.getUserId() == profileId) {
            activity?.let { SPHelper(it).saveProfileData(data) }
        }
        tvProfileName.text = data.display_name
        tvUserName.text = "@" + data.username
        tvAbout.text = data.about
        tvTotalFansCount.text = data.fans
        tvFollowingCount.text = data.followers
        tvFavouritesCount.text = data.favorites
        activity?.let { Common.loadImageUsingURL(imgProfilePic, data.profile_img, it) }
    }


    private fun manageOtherProfile() {
        setTabLayoutAdapter()
        if (Common.getUserId() == profileId) {
            layProfileSubscription.visibility = View.GONE
        } else {
            layProfileSubscription.visibility = View.VISIBLE
            if (profileData.business_type == "FREE") {
                manageFreeProfile()
            } else if (profileData.business_type == "PAID") {
                managePaidProfile()
            }
        }
    }

    private fun managePaidProfile() {
        btnFollowUnFollow.visibility = View.GONE
        laySubscriptionPlan.removeAllViews()
        if (profileData.subscription_plans != null) {
            val filter = profileData.subscription_plans?.filter { it.is_subscribe == "1" }
            if (!filter?.isEmpty()!!) {
                val profileSubscriptionData = filter[0]
                manageCancelSubscribeButtonUI(profileSubscriptionData)
            } else {
                for (subscriptionPlanData in profileData.subscription_plans!!) {
                    manageSubscribeButtonUI(subscriptionPlanData)
                }
            }
        }
    }

    private fun manageSubscribeButtonUI(
        subscriptionPlanData: ProfileSubscriptionData,
    ) {
        val subscriptionButton = Button(activity)
        subscriptionButton.id = subscriptionPlanData.id.toInt()
        subscriptionButton.background = resources.getDrawable(R.drawable.subscription_bg)
        subscriptionButton.text =
            "Subscribe For $" + subscriptionPlanData.amount + "/" + subscriptionPlanData.months + "Months"
        subscriptionButton.setTextColor(resources.getColor(R.color.white))
        val font =
            Typeface.createFromAsset(resources.assets, "cambria.ttf")
        subscriptionButton.typeface = font
        subscriptionButton.setOnClickListener {
            val filterPlan =
                profileData.subscription_plans?.filter { it.id == subscriptionButton.id.toString() }
            if (filterPlan != null) {
                if (filterPlan.isNotEmpty()) {
                    val profileSubscriptionData = filterPlan[0]
                    val paidSubscriptionRequest =
                        PaidSubscriptionRequest(
                            profileSubscriptionData.id,
                            profileSubscriptionData.months,
                            profileSubscriptionData.amount,
                            profileSubscriptionData.profile_id
                        )
                    makePaidSubscriptionAPICall(paidSubscriptionRequest)
                }
            }
        }
        laySubscriptionPlan.addView(subscriptionButton)
    }

    private fun manageCancelSubscribeButtonUI(
        subscriptionPlanData: ProfileSubscriptionData,
    ) {
        val subscriptionButton = Button(activity)
        subscriptionButton.id = subscriptionPlanData.id.toInt()
        subscriptionButton.background = resources.getDrawable(R.drawable.cancel_subscription_bg)
        subscriptionButton.text =
            "Cancel Subscribe For $" + subscriptionPlanData.amount + "/" + subscriptionPlanData.months + "Months"
        subscriptionButton.setTextColor(resources.getColor(R.color.white))
        val font =
            Typeface.createFromAsset(resources.assets, "cambria.ttf")
        subscriptionButton.typeface = font
        subscriptionButton.setOnClickListener {
            val filterPlan =
                profileData.subscription_plans?.filter { it.id == subscriptionButton.id.toString() }
            if (filterPlan != null) {
                if (filterPlan.isNotEmpty()) {
                    val profileSubscriptionData = filterPlan[0]
                    val paidSubscriptionRequest =
                        PaidSubscriptionRequest(
                            profileSubscriptionData.id,
                            "",
                            "",
                            profileSubscriptionData.profile_id,
                            profileSubscriptionData.subscription_id
                        )
                    makeCancelSubscriptionAPICall(paidSubscriptionRequest)
                }
            }
        }
        laySubscriptionPlan.addView(subscriptionButton)
    }

    private fun makePaidSubscriptionAPICall(paidSubscriptionRequest: PaidSubscriptionRequest) {
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.followPaidUser(
            paidSubscriptionRequest, true
        ).observe(viewLifecycleOwner, {
            if (it.status) {
                activity?.let { it1 -> Common.showToast(it1, it.msg) }
                getProfile(true)
            }
        })
//        updatePaidSubscription(paidSubscriptionRequest.plan_id, false)

    }

    private fun makeCancelSubscriptionAPICall(paidSubscriptionRequest: PaidSubscriptionRequest) {
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.unFollowPaidUser(
            paidSubscriptionRequest, true
        ).observe(viewLifecycleOwner, {
            if (it.status) {
                activity?.let { it1 -> Common.showToast(it1, it.msg) }
                getProfile(true)
            }
        })
//        updatePaidSubscription(paidSubscriptionRequest.plan_id)

    }

//    private fun updatePaidSubscription(planId: String, isCancel: Boolean = true) {
//        val subscriptionPlans = profileData.subscription_plans
//        if (subscriptionPlans != null) {
//            for (i in 0..subscriptionPlans.size) {
//                val subscriptionPlanData = subscriptionPlans[i]
//                if (subscriptionPlanData.id == planId) {
//                    if (isCancel) {
//                        subscriptionPlanData.is_subscribe = "0"
//                    } else {
//                        subscriptionPlanData.is_subscribe = "1"
//                    }
//                    subscriptionPlans[i] = subscriptionPlanData
//                    profileData.subscription_plans = subscriptionPlans
//                    if (isCancel) {
//                        profileData.tofollow = "0"
//                    } else {
//                        profileData.tofollow = "1"
//                    }
//                    manageOtherProfile()
//                    break
//                }
//            }
//        }
//
//    }

    private fun manageFreeProfile() {
        btnFollowUnFollow.visibility = View.VISIBLE
        if (profileData.tofollow == "0") {
            btnFollowUnFollow.text = getString(R.string.follow)
        } else {
            btnFollowUnFollow.text = getString(R.string.unfollow)
        }
        btnFollowUnFollow.setOnClickListener {
            val postDetailRequest = ProfileDetailRequest(profileId)
            val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
            baseViewModel.followUser(
                postDetailRequest, true
            ).observe(viewLifecycleOwner, {
                if (it.status) {
                    activity?.let { it1 -> Common.showToast(it1, it.msg) }
                    val toFollow = profileData.tofollow
                    if (toFollow == "0") {
                        btnFollowUnFollow.text = getString(R.string.unfollow)
                    } else {
                        btnFollowUnFollow.text = getString(R.string.follow)
                    }
                    getProfile(true)
                }
            })
        }
    }

    private fun setTabLayoutAdapter() {
        val supportFragmentManager = activity?.supportFragmentManager
        if (supportFragmentManager != null && activity != null) {
            val isProfileVisible = when (profileData.tofollow) {
                "1" -> {
                    true
                }
                else -> {
                    when (profileId) {
                        Common.getUserId() -> {
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
            }
            val tabsPagerAdapter =
                ProfileTabPagerAdapter(requireActivity(),
                    supportFragmentManager,
                    profileId,
                    isProfileVisible)
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
                R.id.imgBack -> {
                    activity?.onBackPressed()
                }
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

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        favoriteUnFavoriteProfile()
    }

    private fun favoriteUnFavoriteProfile() {
        val profileDetailRequest = ProfileDetailRequest(profileId)
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.favoriteProfile(
            profileDetailRequest, true
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        activity?.let { it1 -> Common.showToast(it1, it.msg) }
                    }
                })
    }
}