package com.calendar.loyalfans.viewpager

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.calendar.loyalfans.R
import com.calendar.loyalfans.fragments.profile.FavoritePostFragment
import com.calendar.loyalfans.fragments.profile.FavoriteProfileFragment

@SuppressLint("WrongConstant")
class FavoriteTabPagerAdapter(
    context: Context,
    fm: FragmentManager,
    private val profileId: String,
) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mContext = context
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FavoritePostFragment.newInstance("2", profileId)
            1 -> FavoritePostFragment.newInstance("3", profileId)
            2 -> FavoritePostFragment.newInstance("0", profileId)
            else -> FavoriteProfileFragment.newInstance("1", profileId)
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
                R.string.profile_image,
                R.string.profile_video,
                R.string.profile_post,
                R.string.profile_caps)
    }

}