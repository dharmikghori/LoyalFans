package com.calendar.loyalfans.fragments.ppv

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.activities.PPVActivity
import com.calendar.loyalfans.dialog.FansSelectionDialog
import com.calendar.loyalfans.model.request.ProfileDetailRequest
import com.calendar.loyalfans.model.response.FansData
import com.calendar.loyalfans.model.response.PpvHistoryResponse
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.viewpager.PPVTabPagerAdapter
import kotlinx.android.synthetic.main.fragment_ppv.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*


class PPVFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = PPVFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_ppv, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.ppv_history)
        imgBack.setOnClickListener(this)
        addPPV.setOnClickListener(this)
        getPPVHistory()
    }
    private fun getPPVHistory() {
        val fansFollowingRequest =
            ProfileDetailRequest(Common.getUserId())
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.getPPVHistory(
            fansFollowingRequest,
            true
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        setTabLayoutAdapter(it.data)
                    }
                })
    }

    private fun setTabLayoutAdapter(data: PpvHistoryResponse) {
        val supportFragmentManager = activity?.supportFragmentManager
        if (supportFragmentManager != null && activity != null) {
            val ppvTabPagerAdapter =
                PPVTabPagerAdapter(requireActivity(), supportFragmentManager, data)
            viewPagerPPVMessage.offscreenPageLimit = 2
            viewPagerPPVMessage.adapter = ppvTabPagerAdapter
            ppvTabLayout.setupWithViewPager(viewPagerPPVMessage)
            ppvTabPagerAdapter.notifyDataSetChanged()
            for (i in 0 until ppvTabLayout.tabCount) {
                val tabViewAt = ppvTabLayout.getTabAt(i)?.view
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
                R.id.addPPV -> {
                    (activity as PPVActivity).loadFragment(16)
                }
                R.id.imgBack -> {
                    (activity as PPVActivity).onBackPressed()
                }
            }
        }
    }


}