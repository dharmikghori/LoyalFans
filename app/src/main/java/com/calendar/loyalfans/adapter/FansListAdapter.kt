package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.FansData
import java.util.*

class FansListAdapter(
    private var fansList: ArrayList<FansData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<FansListAdapter.SearchViewHolder>() {


    override fun getItemCount(): Int {
        return fansList.size
    }

    private fun getItem(position: Int): FansData {
        return fansList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_fans_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
    }

    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view)


}


