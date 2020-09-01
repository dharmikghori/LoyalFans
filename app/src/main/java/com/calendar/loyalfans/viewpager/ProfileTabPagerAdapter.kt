package com.calendar.loyalfans.viewpager

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.calendar.loyalfans.R
import com.calendar.loyalfans.fragments.profile.ProfilePhotosFragment
import com.calendar.loyalfans.fragments.profile.ProfilePostFragment
import com.calendar.loyalfans.fragments.profile.ProfileVideosFragment

@SuppressLint("WrongConstant")
class ProfileTabPagerAdapter(context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    private val mContext = context
    private val profilePostFragment = ProfilePostFragment.newInstance()
    private val profilePhotosFragment = ProfilePhotosFragment.newInstance()
    private val profileVideoFragment = ProfileVideosFragment.newInstance()
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> profilePostFragment
            1 -> profilePhotosFragment
            2 -> profileVideoFragment
            else -> profilePhotosFragment
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
            intArrayOf(R.string.profile_post, R.string.profile_image, R.string.profile_video)
    }

}