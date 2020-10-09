package com.calendar.loyalfans.viewpager

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.calendar.loyalfans.R
import com.calendar.loyalfans.fragments.ppv.PPVOtherMessageFragment
import com.calendar.loyalfans.fragments.ppv.PPVMyMessageFragment
import com.calendar.loyalfans.model.response.PpvHistoryResponse

@SuppressLint("WrongConstant")
class PPVTabPagerAdapter(context: Context, fm: FragmentManager, val data: PpvHistoryResponse) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mContext = context
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PPVMyMessageFragment.newInstance(data.my_ppvs)
            else -> PPVOtherMessageFragment.newInstance(data.other_ppvs)
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
            intArrayOf(R.string.my_message,
                R.string.other_message)
    }

}