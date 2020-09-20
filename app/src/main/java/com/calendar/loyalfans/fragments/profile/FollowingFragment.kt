package com.calendar.loyalfans.fragments.profile

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
import com.calendar.loyalfans.viewpager.FansTabPagerAdapter
import com.calendar.loyalfans.viewpager.FollowingTabPagerAdapter
import kotlinx.android.synthetic.main.fragment_fans.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.tvToolBarName

class FollowingFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = FollowingFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_fans, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTabLayoutAdapter()
        tvToolBarName.text = getString(R.string.following)
//        imgBack.setOnClickListener(this)
    }

    private fun setTabLayoutAdapter() {
        val supportFragmentManager = activity?.supportFragmentManager
        if (supportFragmentManager != null && activity != null) {
            val tabsPagerAdapter =
                FollowingTabPagerAdapter(requireActivity(), supportFragmentManager)
            viewPagerFans.offscreenPageLimit = 3
            viewPagerFans.adapter = tabsPagerAdapter
            tabFansLayout.setupWithViewPager(viewPagerFans)
            tabsPagerAdapter.notifyDataSetChanged()
            for (i in 0 until tabFansLayout.tabCount) {
                val tabViewAt = tabFansLayout.getTabAt(i)?.view
                var tabChildCount = tabViewAt?.childCount
                if (tabChildCount == null)
                    tabChildCount = 0
                for (j in 0 until tabChildCount) {
                    if (tabViewAt != null) {
                        val tabViewChild = tabViewAt.getChildAt(j)
                        if (tabViewChild is AppCompatTextView) {
                            tabViewChild.typeface = Typeface.createFromAsset(
                                requireContext().assets,
                                "cambria.ttf"
                            )
                        }
                    }
                }

            }
        }
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