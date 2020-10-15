package com.calendar.loyalfans.fragments.profile

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.viewpager.FansTabPagerAdapter
import kotlinx.android.synthetic.main.fragment_fans.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.*

class FansFragment(private val profileId: String) : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance(profileId: String) = FansFragment(profileId)
        fun newInstance() = FansFragment(Common.getUserId())
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
        tvToolBarName.text = getString(R.string.fans)
//        imgBack.setOnClickListener(this)
    }

    private fun setTabLayoutAdapter() {
        val supportFragmentManager = activity?.supportFragmentManager
        if (supportFragmentManager != null && activity != null) {
            val tabsPagerAdapter =
                FansTabPagerAdapter(requireActivity(), supportFragmentManager, profileId)
            viewPagerFans.offscreenPageLimit = tabsPagerAdapter.count
            viewPagerFans.adapter = tabsPagerAdapter
            tabFansLayout.setupWithViewPager(viewPagerFans)
            tabsPagerAdapter.notifyDataSetChanged()
            Common.setUpTablayOutStyle(tabFansLayout)
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