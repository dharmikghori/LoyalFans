package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_fans_list.view.*
import java.util.*

class FansListAdapter(
    private var fansList: ArrayList<com.calendar.loyalfans.model.response.FansData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<FansListAdapter.FansViewHolder>() {


    override fun getItemCount(): Int {
        return fansList.size
    }

    private fun getItem(position: Int): com.calendar.loyalfans.model.response.FansData {
        return fansList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FansViewHolder {
        return FansViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_fans_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FansViewHolder, position: Int) {
        val fansData = getItem(position)
        holder.tvProfileName.text = fansData.display_name
        holder.tvUserName.text = "@" +fansData.username
        if (fansData.end_date != null) {
            holder.tvExpiryDate.text = "Till " + Common.formatDate(fansData.end_date!!)
        }
        holder.tvExpiryDate.isSelected = true
        activity?.let { Common.loadImageUsingURL(holder.imgProfilePic, fansData.profile_img, it) }
    }

    class FansViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfilePic: CircleImageView = view.imgProfilePic
        val tvProfileName: TextView = view.tvProfileName
        val tvUserName: TextView = view.tvUserName
        val tvExpiryDate: TextView = view.tvExpiryDate
    }

}


