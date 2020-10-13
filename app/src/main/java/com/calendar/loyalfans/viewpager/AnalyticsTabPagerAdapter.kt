package com.calendar.loyalfans.viewpager

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.calendar.loyalfans.R
import com.calendar.loyalfans.fragments.ppv.AnalyticsDetailFragment
import com.calendar.loyalfans.fragments.ppv.AnalyticsEarningFragment
import com.calendar.loyalfans.model.response.MyPPVData

@SuppressLint("WrongConstant")
class AnalyticsTabPagerAdapter(
    context: Context,
    fm: FragmentManager,
    private val analyticsData: MyPPVData,
) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mContext = context
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AnalyticsDetailFragment.newInstance(position, analyticsData)
            1 -> AnalyticsDetailFragment.newInstance(position, analyticsData)
            else -> AnalyticsEarningFragment.newInstance(analyticsData.analytics!!)
        }
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }

    companion object {
        @StringRes
        val TAB_TITLES =
            intArrayOf(R.string.my_fans_analytics,
                R.string.seen_analytics,
                R.string.earning__analytics)
    }

}