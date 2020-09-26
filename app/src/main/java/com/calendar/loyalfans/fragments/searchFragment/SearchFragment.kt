package com.calendar.loyalfans.fragments.searchFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.SearchAdapter
import com.calendar.loyalfans.model.request.SearchUserRequest
import com.calendar.loyalfans.model.response.SearchUsers
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.SPHelper
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.*

class SearchFragment : Fragment(), TextWatcher, View.OnClickListener {

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
        activity?.let { setUpRecentSearchAdapter(SPHelper(it).getRecentSearch()) }
        etSearch.addTextChangedListener(this)
        imgRemove.setOnClickListener(this)
        etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && etSearch.text.toString()
                        .isNotEmpty()
                ) {
                    searchUserByName(etSearch.text.toString())
                    return true
                }
                return false
            }
        })
    }

    private fun setUpRecentSearchAdapter(searchUserList: ArrayList<SearchUsers>) {
        Common.setupVerticalRecyclerView(rvRecentSearch, activity)
        rvRecentSearch.adapter = SearchAdapter(searchUserList, activity, true)
    }

    private fun setUpSearchResultAdapter(searchUserList: ArrayList<SearchUsers>) {
        if (searchUserList.isNotEmpty()) {
            layoutSearchResult.visibility = View.VISIBLE
        } else {
            layoutSearchResult.visibility = View.GONE
        }
        Common.setupVerticalRecyclerView(rvSearchResult, activity)
        rvSearchResult.adapter = SearchAdapter(searchUserList, activity, false)
    }

    private fun searchUserByName(searchName: String) {
        val searchUserRequest = SearchUserRequest(searchName)
        searchUserRequest.user_id = Common.getUserId()
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.searchUser(
            searchUserRequest, true
        )
            .observe(this, {
                if (it.status) {
                    setUpSearchResultAdapter(it.data)
                }
            })
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (etSearch.text.isNotEmpty()) {
            imgRemove.visibility = View.VISIBLE
            searchUserByName(etSearch.text.toString())
        } else {
            imgRemove.visibility = View.INVISIBLE
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun onClick(view: View?) {
        if (view != null) {
            if (view.id == R.id.imgRemove) {
                etSearch.setText("")
                setUpSearchResultAdapter(ArrayList())
                activity?.let { setUpRecentSearchAdapter(SPHelper(it).getRecentSearch()) }
            }
        }
    }

}