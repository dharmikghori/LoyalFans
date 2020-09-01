package com.calendar.loyalfans.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.HomeAdapter
import com.calendar.loyalfans.model.PostData
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
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