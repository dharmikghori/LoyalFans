package com.calendar.loyalfans.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_bottom.*


class MainActivity : BaseActivity() {
    public var drawerLayout: DrawerLayout? = null
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
            type, imgHome, imgSearch, imgNotification, imgProfile,
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

    var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
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
        Common.automaticallyLogoutOnUnauthorizedOrForbidden()
    }

}