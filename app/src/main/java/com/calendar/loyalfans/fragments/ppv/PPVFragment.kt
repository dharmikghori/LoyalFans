package com.calendar.loyalfans.fragments.ppv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.PPVActivity
import com.calendar.loyalfans.model.request.ProfileDetailRequest
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
            viewPagerPPVMessage.offscreenPageLimit = ppvTabPagerAdapter.count
            viewPagerPPVMessage.adapter = ppvTabPagerAdapter
            ppvTabLayout.setupWithViewPager(viewPagerPPVMessage)
            ppvTabPagerAdapter.notifyDataSetChanged()
            Common.setUpTablayOutStyle(ppvTabLayout)
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