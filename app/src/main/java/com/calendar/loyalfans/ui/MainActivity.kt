package com.calendar.loyalfans.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_bottom.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imgHome.performClick()
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


    override fun onBackPressed() {
//        val backStackEntryCount = supportFragmentManager.backStackEntryCount
//        if (backStackEntryCount < 5) {
//            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
        closeDrawer()
        //            } else {
//                finish()
//            }
//        } else {
        super.onBackPressed()
//        }
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

}