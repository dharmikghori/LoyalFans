package com.calendar.loyalfans.fragments.searchFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.SearchAdapter
import com.calendar.loyalfans.model.SearchData
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        tvToolBarName.text = getString(R.string.search)
        setUpAdapter()
    }

    private fun setUpAdapter() {

        rvSearch?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val searchListData = ArrayList<SearchData>()
        for (i in 0..9) {
            searchListData.add(SearchData())
        }
        rvSearch.adapter = SearchAdapter(searchListData, activity)

    }

}