package com.calendar.loyalfans.viewpager

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.calendar.loyalfans.R
import com.calendar.loyalfans.fragments.profile.ProfilePhotosFragment
import com.calendar.loyalfans.fragments.profile.ProfilePostFragment
import com.calendar.loyalfans.fragments.profile.ProfileVideosFragment

@SuppressLint("WrongConstant")
class ProfileTabPagerAdapter(
    context: Context,
    fm: FragmentManager,
    val profileId: String,
    private val isProfileVisible: Boolean,
) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mContext = context
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ProfilePostFragment.newInstance(profileId, isProfileVisible)
            1 -> ProfilePhotosFragment.newInstance(profileId, isProfileVisible)
            else -> ProfileVideosFragment.newInstance(profileId, isProfileVisible)
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
            intArrayOf(R.string.profile_post, R.string.profile_image, R.string.profile_video)
    }

}