package com.calendar.loyalfans.activities

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.RequestParams


class OtherProfileActivity : BaseActivity() {
    var otherProfileId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_profile)
        otherProfileId = intent.getStringExtra(RequestParams.PROFILE_ID).toString()
    }

    override fun onResume() {
        super.onResume()
        loadFragment(5, otherProfileId)

    }


    var fragmentThatWillAddNotReplace: List<Int> = listOf(6, 7, 8, 17)

    fun loadFragment(type: Int, profileId: String) {
        val fragmentToBeLoad = Common.getFragmentBasedOnType(type, profileId)
        val fragmentToTag = Common.getTagBasedOnType(type)
        if (fragmentToBeLoad != null) {
            if (fragmentThatWillAddNotReplace.contains(type)) {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.layFrame, fragmentToBeLoad, fragmentToTag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(fragmentToTag)
                    .commit()
            } else {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.layFrame, fragmentToBeLoad, fragmentToTag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(fragmentToTag)
                    .commit()
            }
        }
    }

    override fun onBackPressed() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if (backStackEntryCount == 1) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

}