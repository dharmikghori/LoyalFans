package com.calendar.loyalfans.fragments.ppv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.adapter.AnalyticsFansAdapter
import com.calendar.loyalfans.adapter.AnalyticsSeenAdapter
import com.calendar.loyalfans.model.request.SendPPVUserRequest
import com.calendar.loyalfans.model.response.FollowersData
import com.calendar.loyalfans.model.response.MyPPVData
import com.calendar.loyalfans.model.response.SeenData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_analytic_details.*

class AnalyticsDetailFragment(
    private val type: Int,
    private val myAnalyticData: MyPPVData,
) : Fragment() {
    private var myFans: ArrayList<FollowersData> = ArrayList()

    companion object {
        fun newInstance(
            type: Int,
            myAnalyticData: MyPPVData,
        ): Fragment {
            return AnalyticsDetailFragment(type, myAnalyticData)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_analytic_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Common.setupVerticalRecyclerView(rvAnalytics, activity)
        when (type) {
            0 -> {
                myFans = myAnalyticData.analytics?.followers!!
                setUpMyFansAdapter()
            }
            1 -> {
                myAnalyticData.analytics?.seen?.let { setUpSeenAdapter(it) }
            }
        }
    }

    private fun setUpSeenAdapter(seenList: java.util.ArrayList<SeenData>) {
        Common.manageNoDataFound(imgNoDataFound, rvAnalytics, seenList.isNullOrEmpty())
        rvAnalytics.adapter = AnalyticsSeenAdapter(seenList, activity)
    }

    private var analyticsFansAdapter: AnalyticsFansAdapter? = null
    private fun setUpMyFansAdapter() {
        Common.manageNoDataFound(imgNoDataFound, rvAnalytics, myFans.isNullOrEmpty())
        Common.setupVerticalRecyclerView(rvAnalytics, activity)
        analyticsFansAdapter = AnalyticsFansAdapter(myFans, activity)
        rvAnalytics.adapter = analyticsFansAdapter
        analyticsFansAdapter!!.onFansSend = object : AnalyticsFansAdapter.OnFansSend {
            override fun onSendToFans(myFollowerData: FollowersData, position: Int) {
                callSendUserAPI(myFollowerData, position)
            }

        }
    }

    private fun callSendUserAPI(myFollowerData: FollowersData, position: Int) {
        val sendPPVUserRequest =
            SendPPVUserRequest(myFollowerData.id, myAnalyticData.details?.id.toString())
        sendPPVUserRequest.user_id = Common.getUserId()
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.ppvSendUser(
            sendPPVUserRequest, true
        ).observe(viewLifecycleOwner,
            {
                if (it.status) {
                    Common.showToast(BaseActivity.getActivity(), it.msg)
                    myFollowerData.sended = "1"
                    myFans[position] = myFollowerData
                    analyticsFansAdapter?.notifyItemChanged(position)
                }
            })
    }


}