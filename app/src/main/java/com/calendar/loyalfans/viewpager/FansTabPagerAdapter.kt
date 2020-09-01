package com.calendar.loyalfans.viewpager

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.calendar.loyalfans.R
import com.calendar.loyalfans.fragments.profile.FansFollowingTypeListFragment

@SuppressLint("WrongConstant")
class FansTabPagerAdapter(context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    private val mContext = context
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FansFollowingTypeListFragment.newInstance()
            1 -> FansFollowingTypeListFragment.newInstance()
            2 -> FansFollowingTypeListFragment.newInstance()
            else -> FansFollowingTypeListFragment.newInstance()
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
            intArrayOf(R.string.active,
                R.string.expired,
                R.string.blocked,
                R.string.all)
    }

}