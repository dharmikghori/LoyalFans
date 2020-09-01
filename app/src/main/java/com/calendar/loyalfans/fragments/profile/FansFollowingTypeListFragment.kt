package com.calendar.loyalfans.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.FansListAdapter
import com.calendar.loyalfans.model.FansData
import kotlinx.android.synthetic.main.fragment_fans_list.*

class FansFollowingTypeListFragment : Fragment() {

    companion object {
        fun newInstance() = FansFollowingTypeListFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_fans_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpFansAdapter()
    }

    private fun setUpFansAdapter() {
        rvFans?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val fansArrayList = ArrayList<FansData>()
        for (i in 0..9) {
            fansArrayList.add(FansData())
        }
        rvFans.adapter = FansListAdapter(fansArrayList, activity)
    }

}