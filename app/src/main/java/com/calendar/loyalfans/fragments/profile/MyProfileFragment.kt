package com.calendar.loyalfans.fragments.profile

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
import com.calendar.loyalfans.ui.MainActivity
import com.calendar.loyalfans.viewpager.ProfileTabPagerAdapter
import kotlinx.android.synthetic.main.layout_profile_body.*
import kotlinx.android.synthetic.main.layout_profile_top_view.*


class MyProfileFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = MyProfileFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_myprofile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTabLayoutAdapter()
        layFans.setOnClickListener(this)
        layFollowing.setOnClickListener(this)
        layFavorites.setOnClickListener(this)
    }

    private fun setTabLayoutAdapter() {
        val supportFragmentManager = activity?.supportFragmentManager
        if (supportFragmentManager != null && activity != null) {
            val tabsPagerAdapter =
                ProfileTabPagerAdapter(requireActivity(), supportFragmentManager)
            viewPager.adapter = tabsPagerAdapter
            tabLayout.setupWithViewPager(viewPager)
            for (i in 0 until tabLayout.tabCount) {
                val tabViewAt = tabLayout.getTabAt(i)?.view
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
                R.id.layFans -> {
                    if (activity is MainActivity) {
                        val mainActivity = activity as MainActivity
                        mainActivity.loadFragment(6)
                    }
                }
                R.id.layFollowing -> {
                    if (activity is MainActivity) {
                        val mainActivity = activity as MainActivity
                        mainActivity.loadFragment(7)
                    }
                }
                R.id.layFavorites -> {
                    if (activity is MainActivity) {
                        val mainActivity = activity as MainActivity
                        mainActivity.loadFragment(8)
                    }
                }
                else -> {
                }
            }
        }
    }
}