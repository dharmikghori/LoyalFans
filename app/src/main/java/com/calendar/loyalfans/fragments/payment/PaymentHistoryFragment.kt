package com.calendar.loyalfans.fragments.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.PaymentHistoryAdapter
import com.calendar.loyalfans.model.request.BaseRequest
import com.calendar.loyalfans.model.response.StatementData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_payment_history.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.tvToolBarName
import java.util.*

class PaymentHistoryFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = PaymentHistoryFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.payment_history)
        imgBack.setOnClickListener(this)
        getPaymentHistory()
    }

    private fun getPaymentHistory() {
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.getPaymentHistory(BaseRequest(), true
        ).observe(viewLifecycleOwner, {
            if (it.status) {
                setUpPayment(it.data)
            }
        })
    }

    private fun setUpPayment(data: ArrayList<StatementData>) {
        Common.setupVerticalRecyclerView(rvPaymentHistory, activity)
        rvPaymentHistory.adapter = PaymentHistoryAdapter(data, activity)
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