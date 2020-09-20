package com.calendar.loyalfans.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.SubscriptionData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_subscription.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*

class SubscriptionFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = SubscriptionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_subscription, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.subscription_plan)
        btnUpdateSubscription.setOnClickListener(this)
        imgBack.setOnClickListener(this)
        getSubscriptionPlans()
    }

    private var subscriptionData: ArrayList<SubscriptionData> = ArrayList()
    private fun getSubscriptionPlans() {
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.getSubscriptionPlan(
            true
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        subscriptionData = it.data
                        setUpSubscriptionData()
                    }
                })
    }

    private fun setUpSubscriptionData() {
        for (subscriptionItem in subscriptionData) {
            when (subscriptionItem.months) {
                "1" -> {
                    etMonth1Amount.setText(subscriptionItem.amount)
                    switchMonth1.isChecked = subscriptionItem.status == "1"
                }
                "3" -> {
                    etMonth3Amount.setText(subscriptionItem.amount)
                    switchMonth3.isChecked = subscriptionItem.status == "1"
                }
                "6" -> {
                    etMonth6Amount.setText(subscriptionItem.amount)
                    switchMonth6.isChecked = subscriptionItem.status == "1"
                }
                "12" -> {
                    etMonth12Amount.setText(subscriptionItem.amount)
                    switchMonth12.isChecked = subscriptionItem.status == "1"
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
                R.id.btnUpdateSubscription -> {
                    onUpdatePlan()
                }
            }
        }
    }

    private fun onUpdatePlan() {
        val updatedDataAndCheckValidation = getUpdatedDataAndCheckValidation()
        val strSubscriptionData = Gson().toJson(updatedDataAndCheckValidation)
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.setSubscriptionPlan(
            strSubscriptionData,
            true
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        activity?.let { it1 ->
                            com.calendar.loyalfans.utils.Common.showToast(it1,
                                it.msg)
                        }
                        imgBack.performClick()
                    }
                })
    }

    private fun getUpdatedDataAndCheckValidation(): ArrayList<SubscriptionData> {
        val userId = com.calendar.loyalfans.utils.Common.getUserId()
        val updateSubscriptionList: ArrayList<SubscriptionData> = ArrayList()
        val subscriptionUpdatedData1 = SubscriptionData()
        subscriptionUpdatedData1.status = when (switchMonth1.isChecked) {
            true -> "1"
            else -> "0"
        }
        subscriptionUpdatedData1.amount = when (etMonth1Amount.text.toString().isEmpty()) {
            true -> "0"
            else -> etMonth1Amount.text.toString()
        }
        subscriptionUpdatedData1.months = "1"
        subscriptionUpdatedData1.user_id = userId

        val subscriptionUpdatedData3 = SubscriptionData()
        subscriptionUpdatedData3.status = when (switchMonth3.isChecked) {
            true -> "1"
            else -> "0"
        }
        subscriptionUpdatedData3.amount = when (etMonth3Amount.text.toString().isEmpty()) {
            true -> "0"
            else -> etMonth3Amount.text.toString()
        }
        subscriptionUpdatedData3.months = "3"
        subscriptionUpdatedData3.user_id = userId

        val subscriptionUpdatedData6 = SubscriptionData()
        subscriptionUpdatedData6.status = when (switchMonth6.isChecked) {
            true -> "1"
            else -> "0"
        }
        subscriptionUpdatedData6.amount = when (etMonth6Amount.text.toString().isEmpty()) {
            true -> "0"
            else -> etMonth6Amount.text.toString()
        }
        subscriptionUpdatedData6.months = "6"
        subscriptionUpdatedData6.user_id = userId

        val subscriptionUpdatedData12 = SubscriptionData()
        subscriptionUpdatedData12.status = when (switchMonth12.isChecked) {
            true -> "1"
            else -> "0"
        }
        subscriptionUpdatedData12.amount = when (etMonth12Amount.text.toString().isEmpty()) {
            true -> "0"
            else -> etMonth12Amount.text.toString()
        }
        subscriptionUpdatedData12.months = "12"
        subscriptionUpdatedData12.user_id = userId


        updateSubscriptionList.add(subscriptionUpdatedData1)
        updateSubscriptionList.add(subscriptionUpdatedData3)
        updateSubscriptionList.add(subscriptionUpdatedData6)
        updateSubscriptionList.add(subscriptionUpdatedData12)
        return updateSubscriptionList
    }
}