package com.calendar.loyalfans.fragments.notifications

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.MainActivity
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.viewpager.NotificationTabPagerAdapter
import com.calendar.loyalfans.viewpager.NotificationTabPagerAdapter.Companion.TAB_TITLES
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.*

class NotificationFragment() : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = NotificationFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTabLayoutAdapter()
        tvToolBarName.text = getString(R.string.notifications)
//        imgBack.setOnClickListener(this)
    }

    private fun setTabLayoutAdapter() {
        val supportFragmentManager = activity?.supportFragmentManager
        if (supportFragmentManager != null && activity != null) {
            val tabsPagerAdapter =
                NotificationTabPagerAdapter(requireActivity(), supportFragmentManager)
            viewPagerNotification.offscreenPageLimit = TAB_TITLES.size
            viewPagerNotification.adapter = tabsPagerAdapter
            tabNotificationLayout.setupWithViewPager(viewPagerNotification)
            tabsPagerAdapter.notifyDataSetChanged()
            Common.setUpTablayOutStyle(tabNotificationLayout)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).manageBottomNavigationVisibility(true)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.imgBack -> {
                    activity?.onBackPressed()
                }
            }
        }
    }
}