package com.calendar.loyalfans.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.calendar.loyalfans.R
import com.calendar.loyalfans.fragments.ppv.OtherMessageDetailsFragment
import com.calendar.loyalfans.model.response.OtherPPVData
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
                .add(R.id.layFrame, fragmentToBeLoad, fragmentToTag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit()
        }
    }

    fun loadFragment(fragmentToBeLoad: Fragment, fragmentToTag: String) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.layFrame, fragmentToBeLoad, fragmentToTag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }


    override fun onBackPressed() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if (backStackEntryCount == 1) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }

    }

    fun loadFragment(type: Int, otherPPVData: OtherPPVData) {
        val fragmentToBeLoad = OtherMessageDetailsFragment.newInstance(otherPPVData)
        val fragmentToTag = Common.getTagBasedOnType(type)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.layFrame, fragmentToBeLoad, fragmentToTag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }

}