package com.calendar.loyalfans.fragments.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.MainActivity
import com.calendar.loyalfans.model.response.BankListData
import kotlinx.android.synthetic.main.fragment_bank_transfer_w9.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.tvToolBarName


class BankTransferAndW9Fragment(private val bankItem: BankListData) : Fragment(),
    View.OnClickListener {

    companion object {
        fun newInstance(bankList: BankListData) = BankTransferAndW9Fragment(bankList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_bank_transfer_w9, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.banking)
        imgBack.setOnClickListener(this)
        tvW9Form.setOnClickListener(this)
        tvBankTransfer.setOnClickListener(this)
        setUpBankList()
    }

    private fun setUpBankList() {
        tvNameBank.text = bankItem.first_name + " " + bankItem.last_name
        tvSSNBank.text = bankItem.ssn_num
        tvPhoneBank.text = bankItem.phone
        tvStreetBank.text = bankItem.street
        tvCityBank.text = bankItem.city
        tvStateBank.text = bankItem.state
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.imgBack -> {
                    activity?.onBackPressed()
                }
                R.id.tvW9Form -> {
                    (activity as MainActivity).loadFragment(20)
                }
                R.id.tvBankTransfer -> {
                    (activity as MainActivity).loadFragment(21)
                }
            }
        }
    }


}