package com.calendar.loyalfans.activities

import android.os.Bundle
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common


class PPVActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppv)
        loadFragment(15)
    }

    fun loadFragment(type: Int) {
        val fragmentToBeLoad = Common.getFragmentBasedOnType(type)
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
        super.onBackPressed()
        super.onBackPressed()
    }

}