package com.calendar.loyalfans.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.FansListAdapter
import com.calendar.loyalfans.model.request.BlockUnblockRequest
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
    }

    override fun onResume() {
        super.onResume()
        getFansByType(fansType)

    }

    private fun getFansByType(fansType: String) {
        val fansFollowingRequest =
            FansFollowingRequest(fansType,
                profileId)
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.getFansByType(
            fansFollowingRequest,
            false
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        fansList = it.data
                        setUpFansAdapter()
                    } else {
                        Common.manageNoDataFound(imgNoDataFound, rvFans, true)
                    }
                })
    }

    private var fansList: ArrayList<FansData> = ArrayList()
    private var fansListAdapter: FansListAdapter? = null
    private fun setUpFansAdapter() {
        Common.manageNoDataFound(imgNoDataFound, rvFans, fansList.isNullOrEmpty())
        Common.setupVerticalRecyclerView(rvFans, activity)
        fansListAdapter = FansListAdapter(fansList, activity, fansType)
        rvFans.adapter = fansListAdapter
        fansListAdapter!!.onFansOptions = object : FansListAdapter.OnFansOptions {
            override fun onFansBlock(fansData: FansData, position: Int) {
                onBlockUnblockFans(fansData, position, "1")
            }

            override fun onFansUnBlock(fansData: FansData, position: Int) {
                onBlockUnblockFans(fansData, position, "2")
            }
        }
    }

    private fun onBlockUnblockFans(fansData: FansData, position: Int, blockStatus: String) {
        val blockUnblockRequest =
            BlockUnblockRequest(fansData.fanid, blockStatus)
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.blockUser(
            blockUnblockRequest,
            true
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        activity?.let { it1 -> Common.showToast(it1, it.msg) }
                        if (fansType == "2" || fansType == "3") {
                            fansList.removeAt(position)
                        } else if (fansType == "0") {
                            val fansDataOjb = fansList[position]
                            fansDataOjb.block = when (fansDataOjb.block) {
                                "0" -> {
                                    "1"
                                }
                                "1" -> {
                                    "0"
                                }
                                else -> ""
                            }
                            fansList[position] = fansDataOjb
                        }
                        fansListAdapter?.updateFansList(fansList)
                        Common.manageNoDataFound(imgNoDataFound, rvFans, fansList.isNullOrEmpty())
                    }
                })
    }

}