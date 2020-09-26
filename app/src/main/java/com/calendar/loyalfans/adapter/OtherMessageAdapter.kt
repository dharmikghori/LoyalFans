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
import kotlinx.android.synthetic.main.layout_other_message.view.*
import java.util.*

class OtherMessageAdapter(
    private var fansList: ArrayList<com.calendar.loyalfans.model.response.OtherPPVData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<OtherMessageAdapter.FansViewHolder>() {


    override fun getItemCount(): Int {
        return fansList.size
    }

    private fun getItem(position: Int): com.calendar.loyalfans.model.response.OtherPPVData {
        return fansList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FansViewHolder {
        return FansViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_other_message,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FansViewHolder, position: Int) {
        val otherMessageData = getItem(position)
        holder.tvProfileName.text = otherMessageData.display_name
        holder.tvMessageContent.text = otherMessageData.details?.content
        holder.tvDateAndTime.text =
            otherMessageData.details?.updated_at?.let { Common.formatTime(it) }
        activity?.let {
            Common.loadImageUsingURL(holder.imgProfilePic,
                otherMessageData.profile_img,
                it)
        }
    }

    class FansViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfilePic: CircleImageView = view.imgProfilePic
        val tvProfileName: TextView = view.tvProfileName
        val tvMessageContent: TextView = view.tvMessageContent
        val tvDateAndTime: TextView = view.tvDateAndTime
    }

}


