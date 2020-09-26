package com.calendar.loyalfans.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.SearchUsers
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.SPHelper
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_search.view.*
import java.util.*

class SearchAdapter(
    private var searchListData: ArrayList<SearchUsers>,
    private val activity: Context?,
    private val isCloseVisible: Boolean,
) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {


    override fun getItemCount(): Int {
        return searchListData.size
    }


    private fun getItem(position: Int): SearchUsers {
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
        val searchItem = getItem(position)
        holder.tvProfileName.text = searchItem.display_name
        holder.tvUserName.text = searchItem.username
        activity?.let {
            Common.loadImageUsingURL(holder.imgProfilePic,
                searchItem.profile_img, it)
        }
        holder.laySearchUser.setOnClickListener {
            activity?.let { it1 ->
                val spHelper = SPHelper(it1)
                spHelper.saveRecentSearch(searchItem)
            }
        }
        if (isCloseVisible) {
            holder.imgRemove.visibility = View.VISIBLE
        } else {
            holder.imgRemove.visibility = View.GONE
        }
        holder.imgRemove.setOnClickListener {
            activity?.let { it1 ->
                val spHelper = SPHelper(it1)
                spHelper.removeRecentSearch(searchItem)
                searchListData.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfilePic: CircleImageView = view.imgProfilePic
        val tvProfileName: TextView = view.tvProfileName
        val tvUserName: TextView = view.tvUserName
        val laySearchUser: LinearLayout = view.laySearchUser
        val imgRemove: ImageView = view.imgRemove

    }


}


