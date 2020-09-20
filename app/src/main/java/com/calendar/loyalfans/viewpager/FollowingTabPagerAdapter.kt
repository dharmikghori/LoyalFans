package com.calendar.loyalfans.viewpager

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.calendar.loyalfans.R
import com.calendar.loyalfans.fragments.profile.FollowingTypeListFragment

@SuppressLint("WrongConstant")
class FollowingTabPagerAdapter(context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mContext = context
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FollowingTypeListFragment.newInstance("1")
            1 -> FollowingTypeListFragment.newInstance("0")
            else -> FollowingTypeListFragment.newInstance("2")
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
        private val TAB_TITLES =
            intArrayOf(
                R.string.active,
                R.string.expired,
                R.string.blocked)
    }

}