package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.PostData
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.layout_post_top_view.view.*
import java.util.*

class HomeAdapter(
    private var postListData: ArrayList<PostData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<HomeAdapter.HomePostViewHolder>() {


    override fun getItemCount(): Int {
        return postListData.size
    }


    private fun getItem(position: Int): PostData {
        return postListData[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePostViewHolder {
        return HomePostViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_home,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomePostViewHolder, position: Int) {
        holder.btnSendTip.setOnClickListener { activity?.let { Common.showSendDialog(it) } }
    }

    class HomePostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnSendTip = view.btnSendTip
    }


}


