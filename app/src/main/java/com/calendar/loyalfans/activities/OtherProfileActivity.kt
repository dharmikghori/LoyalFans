package com.calendar.loyalfans.activities

import android.os.Bundle
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

    fun loadFragment(type: Int, profileId: String) {
        val fragmentToBeLoad = Common.getFragmentBasedOnType(type, profileId)
        val fragmentToTag = Common.getTagBasedOnType(type)
        if (fragmentToBeLoad != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.layFrame, fragmentToBeLoad, fragmentToTag)
                .addToBackStack(fragmentToTag)
                .commit()
        }
    }

    override fun onBackPressed() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        supportFragmentManager.popBackStack()
        if (backStackEntryCount == 1) {
            finish()
        }
    }

}