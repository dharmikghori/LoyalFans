package com.calendar.loyalfans.fragments.payment

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.viewpager.StatementTabPagerAdapter
import kotlinx.android.synthetic.main.fragment_statements.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.tvToolBarName

class StatementFragment : Fragment(), View.OnClickListener {

    companion object {

        fun newInstance() = StatementFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_statements, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTabLayoutAdapter()
        setViewPagerHeight()
        tvToolBarName.text = getString(R.string.statements)
        imgBack.setOnClickListener(this)
        btnRequestWithdrawal.setOnClickListener(this)
        BaseActivity.currentBalance.value = ""
        BaseActivity.currentBalance.observe(viewLifecycleOwner, {
            var currentBal = 0.0F
            if (it != "") {
                currentBal = it.toFloat()
            }

            tvTotalBalance.text = "$ ${"%.2f".format(currentBal)}"
        })
    }

    private fun setTabLayoutAdapter() {
        val supportFragmentManager = activity?.supportFragmentManager
        if (supportFragmentManager != null && activity != null) {
            val tabsPagerAdapter =
                StatementTabPagerAdapter(requireActivity(), supportFragmentManager)
            viewPagerStatements.offscreenPageLimit = tabsPagerAdapter.count
            viewPagerStatements.adapter = tabsPagerAdapter
            tabStatements.setupWithViewPager(viewPagerStatements)
            tabsPagerAdapter.notifyDataSetChanged()
            Common.setUpTablayOutStyle(tabStatements)
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.imgBack -> {
                    activity?.onBackPressed()
                }
                R.id.btnRequestWithdrawal -> {
                    activity?.let { Common.withdrawalRequestDialog(it) }
                }
            }
        }
    }

    private var isFirstTime = true

    private fun setViewPagerHeight() {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val totalHeight = displayMetrics.heightPixels
        if (isFirstTime) {
            isFirstTime = false
            var layWithdrawHeight = layWithdraw.measuredHeight
            var tabLayoutHeight = tabStatements.measuredHeight
            if (layWithdrawHeight == 0) {
                layWithdrawHeight = 150
            }
            if (tabLayoutHeight == 0) {
                tabLayoutHeight = 150
            }
            val heightForViewPager =
                totalHeight - (layWithdrawHeight + tabLayoutHeight + 50)
            val layoutParams = viewPagerStatements.layoutParams
            layoutParams.height = heightForViewPager
            viewPagerStatements.layoutParams = layoutParams
        }
    }

}