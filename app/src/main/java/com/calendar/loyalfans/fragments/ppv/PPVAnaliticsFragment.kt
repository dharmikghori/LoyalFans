package com.calendar.loyalfans.fragments.ppv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.PPVActivity
import com.calendar.loyalfans.model.response.MyPPVData
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.viewpager.AnalyticsTabPagerAdapter
import kotlinx.android.synthetic.main.fragment_ppv_analytic.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*


class PPVAnaliticsFragment(private val myPPVData: MyPPVData) : Fragment(),
    View.OnClickListener {

    companion object {
        fun newInstance(myPPVData: MyPPVData) = PPVAnaliticsFragment(myPPVData)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_ppv_analytic, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.analytics)
        imgBack.setOnClickListener(this)
        setTabLayoutAdapter()
    }


    private fun setTabLayoutAdapter() {
        val supportFragmentManager = activity?.supportFragmentManager
        if (supportFragmentManager != null && activity != null) {
            val analyticsTabPager =
                AnalyticsTabPagerAdapter(requireActivity(), supportFragmentManager, myPPVData)
            viewPagerAnalytics.offscreenPageLimit = analyticsTabPager.count
            viewPagerAnalytics.adapter = analyticsTabPager
            analyticsTabLayout.setupWithViewPager(viewPagerAnalytics)
            analyticsTabPager.notifyDataSetChanged()
            Common.setUpTablayOutStyle(analyticsTabLayout)
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.imgBack -> {
                    (activity as PPVActivity).onBackPressed()
                }
            }
        }
    }


}