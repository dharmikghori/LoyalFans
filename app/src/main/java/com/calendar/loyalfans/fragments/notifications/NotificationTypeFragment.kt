package com.calendar.loyalfans.fragments.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.NotificationAdapter
import com.calendar.loyalfans.model.request.NotificationSecurityRequest
import com.calendar.loyalfans.model.response.NotificationData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_fans_list.*

class NotificationTypeFragment(private val notificationType: String) :
    Fragment() {

    companion object {
        fun newInstance(notificationType: String): Fragment {
            return NotificationTypeFragment(notificationType)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_fans_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getFansByType(notificationType)
    }

    private fun getFansByType(fansType: String) {
        val notificationSecurityRequest =
            NotificationSecurityRequest(fansType)
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.notificationList(
            notificationSecurityRequest,
            true
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        setUpNotificationAdapter(it.data)
                    } else {
                        Common.manageNoDataFound(imgNoDataFound, rvFans, true)
                    }
                })
    }


    private fun setUpNotificationAdapter(notificationList: ArrayList<NotificationData>) {
        Common.manageNoDataFound(imgNoDataFound, rvFans, notificationList.isNullOrEmpty())
        Common.setupVerticalRecyclerView(rvFans, activity)
        rvFans.adapter = NotificationAdapter(notificationList, activity)
    }

}