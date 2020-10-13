package com.calendar.loyalfans.fragments.ppv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.MyPPVAnalyticData
import kotlinx.android.synthetic.main.fragment_analytic_earning.*

class AnalyticsEarningFragment(
    private val myAnalyticData: MyPPVAnalyticData,
) : Fragment() {

    companion object {
        fun newInstance(
            myAnalyticData: MyPPVAnalyticData,
        ): Fragment {
            return AnalyticsEarningFragment(myAnalyticData)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_analytic_earning, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvTotalSent.text = myAnalyticData.earning?.send
        tvTotalSeen.text = myAnalyticData.earning?.seen
        tvTotalEarning.text = myAnalyticData.earning?.earning
    }
}