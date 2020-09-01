package com.calendar.loyalfans.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.HomeAdapter
import com.calendar.loyalfans.model.PostData
import kotlinx.android.synthetic.main.fragment_home.*

class ProfilePostFragment : Fragment() {

    companion object {
        fun newInstance() = ProfilePostFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_post, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpAdapter()
    }

    private fun setUpAdapter() {
        rvHomePost?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val postList = ArrayList<PostData>()
        for (i in 0..9) {
            postList.add(PostData())
        }
        val homeAdapter = HomeAdapter(postList, activity)
        rvHomePost.adapter = homeAdapter
    }

}