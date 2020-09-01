package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.SearchData
import java.util.*

class SearchAdapter(
    private var searchListData: ArrayList<SearchData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {


    override fun getItemCount(): Int {
        return searchListData.size
    }


    private fun getItem(position: Int): SearchData {
        return searchListData[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_search,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
    }

    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view)


}


