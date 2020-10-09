package com.calendar.loyalfans.viewpager

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.calendar.loyalfans.R
import com.calendar.loyalfans.fragments.notifications.NotificationTypeFragment

@SuppressLint("WrongConstant")
class NotificationTabPagerAdapter(context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mContext = context
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> NotificationTypeFragment.newInstance("0")
            1 -> NotificationTypeFragment.newInstance("1")
            2 -> NotificationTypeFragment.newInstance("2")
            3 -> NotificationTypeFragment.newInstance("3")
            else -> NotificationTypeFragment.newInstance("4")
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
            intArrayOf(R.string.all,
                R.string.interactions,
                R.string.liked,
                R.string.subscribed,
                R.string.tipped)
    }

}