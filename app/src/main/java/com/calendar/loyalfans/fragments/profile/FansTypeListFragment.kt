package com.calendar.loyalfans.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.FansListAdapter
import com.calendar.loyalfans.model.request.FansFollowingRequest
import com.calendar.loyalfans.model.response.FansData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_fans_list.*

class FansTypeListFragment(private val fansType: String, private val profileId: String) :
    Fragment() {

    companion object {
        fun newInstance(fansTpe: String, profileId: String): Fragment {
            return FansTypeListFragment(fansTpe, profileId)
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
        getFansByType(fansType)
    }

    private fun getFansByType(fansType: String) {
        val fansFollowingRequest =
            FansFollowingRequest(fansType,
                profileId)
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.getFansByType(
            fansFollowingRequest,
            true
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        setUpFansAdapter(it.data)
                    }
                })
    }

    private fun setUpFansAdapter(fansList: ArrayList<FansData>) {
        Common.setupVerticalRecyclerView(rvFans, activity)
        rvFans.adapter = FansListAdapter(fansList, activity)
    }

}