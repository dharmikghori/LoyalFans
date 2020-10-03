package com.calendar.loyalfans.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.fragments.payment.BankTransferAndW9Fragment
import com.calendar.loyalfans.fragments.post.EditPostFragment
import com.calendar.loyalfans.model.request.ProfileDetailRequest
import com.calendar.loyalfans.model.response.BankListData
import com.calendar.loyalfans.model.response.PostData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.SPHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.layout_bottom.*


class MainActivity : BaseActivity() {
    var drawerLayout: DrawerLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imgHome.performClick()
        tvUserNameMenu.text = spHelper.getLoginData()?.data?.username
        drawerLayout = drawer_layout
    }

    fun loadFragment(type: Int) {
        val fragmentToBeLoad = Common.getFragmentBasedOnType(type)
        val fragmentToTag = Common.getTagBasedOnType(type)
        Common.manageBottomVisibilitiesAndSelectionBasedOnType(
            type, imgHome, imgSearch, imgNotification,
            resources, theme
        )
        closeDrawer()
        if (fragmentToBeLoad != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.layFrame, fragmentToBeLoad, fragmentToTag)
                .addToBackStack(fragmentToTag)
                .commit()
        }
    }


    //paramsId = will be userID, PostId
    fun loadFragment(type: Int, paramsId: String) {
        val fragmentToBeLoad = Common.getFragmentBasedOnType(type, paramsId)
        val fragmentToTag = Common.getTagBasedOnType(type)
        if (fragmentToBeLoad != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.layFrame, fragmentToBeLoad, fragmentToTag)
                .addToBackStack(fragmentToTag)
                .commit()
        }
    }

    fun loadFragment(type: Int, bankList: BankListData) {
        val fragmentToBeLoad = BankTransferAndW9Fragment.newInstance(bankList)
        val fragmentToTag = Common.getTagBasedOnType(type)
        closeDrawer()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.layFrame, fragmentToBeLoad, fragmentToTag)
            .addToBackStack(fragmentToTag)
            .commit()
    }

    fun loadFragmentWithEditPostListener(
        type: Int,
        postData: PostData,
        onPostEdit: EditPostFragment.OnPostEdit,
    ) {
        val fragmentToTag = Common.getTagBasedOnType(type)
        Common.manageBottomVisibilitiesAndSelectionBasedOnType(
            type, imgHome, imgSearch, imgNotification,
            resources, theme
        )
        closeDrawer()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.layFrame,
                EditPostFragment.newInstance(postData, onPostEdit), fragmentToTag)
            .addToBackStack(fragmentToTag)
            .commit()
    }

    var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        manageBottomNavigationVisibility(true)
        val isMainFragment = checkMainLastFragmentOrNot()
        if (isMainFragment) {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                closeDrawer()
            } else if (doubleBackToExitPressedOnce) {
                finish()
            }
            doubleBackToExitPressedOnce = true
            Common.showToast(this, getString(R.string.back_press_message))
            Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false },
                2000)
        } else {
            super.onBackPressed()
        }
    }

    fun manageBottomNavigationVisibility(isVisible: Boolean) {
        if (isVisible) {
            layBottomNavigation.visibility = View.VISIBLE
        } else {
            layBottomNavigation.visibility = View.GONE
        }
    }

    private fun checkMainLastFragmentOrNot(): Boolean {
        val fragmentHome = supportFragmentManager.findFragmentByTag(Common.getTagBasedOnType(1))
        val fragmentSearch = supportFragmentManager.findFragmentByTag(Common.getTagBasedOnType(2))
        val fragmentAddPost = supportFragmentManager.findFragmentByTag(Common.getTagBasedOnType(3))
        val fragmentNotification =
            supportFragmentManager.findFragmentByTag(Common.getTagBasedOnType(4))
        val fragmentMyProfile =
            supportFragmentManager.findFragmentByTag(Common.getTagBasedOnType(5))

        return if (fragmentHome != null && fragmentHome.isVisible) {
            true
        } else if (fragmentSearch != null && fragmentSearch.isVisible) {
            true
        } else if (fragmentAddPost != null && fragmentAddPost.isVisible) {
            true
        } else if (fragmentNotification != null && fragmentNotification.isVisible) {
            true
        } else fragmentMyProfile != null && fragmentMyProfile.isVisible
    }

    private fun closeDrawer() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    fun onHome(view: View) {
        loadFragment(1)
    }

    fun onSearch(view: View) {
        loadFragment(2)
    }

    fun onAddPost(view: View) {
        loadFragment(3)
    }

    fun onNotification(view: View) {
        loadFragment(4)
    }

    fun onProfile(view: View) {
        loadFragment(5)
    }

    fun onChangePassword(view: View) {
        loadFragment(9)
    }

    fun onEditProfile(view: View) {
        loadFragment(10)
    }

    fun onNotificationSetting(view: View) {
        loadFragment(11)
    }

    fun onSecurity(view: View) {
        loadFragment(12)
    }

    fun onAddCard(view: View) {
        loadFragment(13)
    }

    fun onLogout(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.logout)
        builder.setMessage(R.string.logout_message)
        builder.setIcon(R.mipmap.ic_logout)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            Common.automaticallyLogoutOnUnauthorizedOrForbidden()
        }
        builder.setNegativeButton("Cancel") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    fun onSubscriptionPlan(view: View) {
        loadFragment(14)
    }

    fun onBankAdd(view: View) {
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.bankList(true).observe(this, {
            if (it.bank_status) {
                loadFragment(19, it.data)
            } else {
                loadFragment(18)
            }
        })
    }


    override fun onResume() {
        super.onResume()
        getProfile()
    }


    private fun getProfile() {
        val postDetailRequest = ProfileDetailRequest(Common.getUserId())
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.getProfile(
            postDetailRequest, false
        ).observe(this, {
            if (it.status) {
                SPHelper(this).saveProfileData(it.data)
                tvUserNameMenu.text = it.data.username
                Common.loadImageUsingURL(imgProfileBottom, it.data.profile_img, this)
            }
        })

    }
}