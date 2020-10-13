package com.calendar.loyalfans.fragments.ppv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.OtherDetailsMessageAdapter
import com.calendar.loyalfans.model.request.NotificationSecurityRequest
import com.calendar.loyalfans.model.response.MyPPVDetailsData
import com.calendar.loyalfans.model.response.OtherPPVData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_other_messages.*
import kotlinx.android.synthetic.main.layout_toolbar_other_message.*

class OtherMessageDetailsFragment(private val otherPPVData: OtherPPVData) : Fragment() {

    companion object {
        fun newInstance(otherPPVData: OtherPPVData) = OtherMessageDetailsFragment(otherPPVData)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_other_messages, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            Common.loadImageUsingURL(imgProfilePic,
                otherPPVData.profile_img,
                it,
                true)
        }
        tvProfileName.text = otherPPVData.display_name
        tvUserName.text = "@" + otherPPVData.username
        imgBack.setOnClickListener {
            activity?.onBackPressed()
        }
        setUpOtherMessagesAdapter()
    }

    private fun callMessageSeenAPI(ppvID: String) {
        val baseViewModel =
            ViewModelProvider(this).get(BaseViewModel::class.java)
        val payPPVRequest = NotificationSecurityRequest(ppvID)
        baseViewModel.seenPPV(payPPVRequest, false).observe(viewLifecycleOwner, { })
    }

    private var otherDetailsMessageAdapter: OtherDetailsMessageAdapter? = null
    private fun setUpOtherMessagesAdapter() {
        Common.setupVerticalRecyclerView(rvOtherMessages, activity)
        otherDetailsMessageAdapter =
            otherPPVData.details?.let { OtherDetailsMessageAdapter(it, activity) }
        rvOtherMessages.adapter = otherDetailsMessageAdapter
        otherDetailsMessageAdapter?.onPayPPV = object : OtherDetailsMessageAdapter.OnPayPPVPost {
            override fun onPay(otherPPVData: MyPPVDetailsData, position: Int) {
                onPayForPPVPost(otherPPVData, position)
            }

            override fun onFreePostSeen(otherPPVData: MyPPVDetailsData) {
                if (otherPPVData.paid != "1") {
                    callMessageSeenAPI(otherPPVData.id)
                }
            }
        }
    }

    private fun onPayForPPVPost(myPPVMessageData: MyPPVDetailsData, position: Int) {
        val baseViewModel =
            ViewModelProvider(this).get(BaseViewModel::class.java)
        val payPPVRequest = NotificationSecurityRequest(myPPVMessageData.id)
        baseViewModel.payPPVPost(payPPVRequest, true).observe(viewLifecycleOwner, {
            if (it.status) {
                activity?.let { it1 -> Common.showToast(it1, it.msg) }
                myPPVMessageData.paid = "1"
                otherPPVData.details?.set(position, myPPVMessageData)
                otherDetailsMessageAdapter?.notifyItemChanged(position)
                callMessageSeenAPI(myPPVMessageData.id)
            }
        })
    }


}