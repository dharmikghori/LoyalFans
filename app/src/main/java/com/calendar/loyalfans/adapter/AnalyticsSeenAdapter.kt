package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.SeenData
import com.calendar.loyalfans.utils.Common
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_analytics.view.*
import kotlinx.android.synthetic.main.layout_fans_list.view.imgProfilePic
import kotlinx.android.synthetic.main.layout_fans_list.view.tvProfileName
import kotlinx.android.synthetic.main.layout_fans_list.view.tvUserName
import java.util.*

class AnalyticsSeenAdapter(
    private var seenList: ArrayList<SeenData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<AnalyticsSeenAdapter.FansViewHolder>() {


    override fun getItemCount(): Int {
        return seenList.size
    }

    private fun getItem(position: Int): SeenData {
        return seenList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FansViewHolder {
        return FansViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_analytics,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FansViewHolder, position: Int) {
        val fansData = getItem(position)
        holder.tvProfileName.text = fansData.display_name
        holder.tvUserName.text = "@" + fansData.username
        activity?.let { Common.loadImageUsingURL(holder.imgProfilePic, fansData.profile_img, it) }
    }

    class FansViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfilePic: CircleImageView = view.imgProfilePic
        val tvProfileName: TextView = view.tvProfileName
        val tvUserName: TextView = view.tvUserName
        val btnSend: Button = view.btnSend
        val imgSendStatus: ImageView = view.imgSendStatus
    }

}


