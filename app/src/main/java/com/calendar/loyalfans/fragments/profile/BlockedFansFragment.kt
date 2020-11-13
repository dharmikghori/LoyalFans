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
import com.calendar.loyalfans.model.response.FansData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_blocked_users.*
import kotlinx.android.synthetic.main.fragment_fans_list.imgNoDataFound
import kotlinx.android.synthetic.main.fragment_fans_list.rvFans
import kotlinx.android.synthetic.main.layout_toolbar_textview.*

class BlockedFansFragment() :
    Fragment() {

    companion object {
        fun newInstance(): Fragment {
            return BlockedFansFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_blocked_users, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        toolBarFans.visibility = View.VISIBLE
        tvToolBarName.text = getString(R.string.blocked_users)
        getFansByType()
    }

    private fun getFansByType() {

        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.getBlockUserList(
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
        fansListAdapter = FansListAdapter(fansList, activity, "3")
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
                        fansList.removeAt(position)
                        fansListAdapter?.updateFansList(fansList)
                        Common.manageNoDataFound(imgNoDataFound, rvFans, fansList.isNullOrEmpty())
                    }
                })
    }

}