package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.OtherPPVData
import com.calendar.loyalfans.utils.Common
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_other_message.view.*
import java.util.*

class OtherMessageAdapter(
    private var messages: ArrayList<OtherPPVData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<OtherMessageAdapter.FansViewHolder>() {
    var onOtherMessageAction: OnOtherMessageAction? = null

    interface OnOtherMessageAction {
        fun otherMessage(otherPPVData: OtherPPVData)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    private fun getItem(position: Int): OtherPPVData {
        return messages[position]
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
        val details = otherMessageData.details?.last()
        holder.tvProfileName.text = otherMessageData.display_name
        holder.tvMessageContent.text = details?.content
        if (otherMessageData.unseen != "0") {
            holder.tvUnReadCount.text = otherMessageData.unseen
            holder.tvUnReadCount.visibility = View.VISIBLE
            if (activity != null) {
                holder.tvMessageContent.setTextColor(activity.resources.getColor(R.color.black))
            }
        } else {
            holder.tvUnReadCount.visibility = View.GONE
            if (activity != null) {
                holder.tvMessageContent.setTextColor(activity.resources.getColor(R.color.theme_black))
            }

        }
        holder.layOtherMessage.setOnClickListener {
            onOtherMessageAction?.otherMessage(otherMessageData)
        }
        holder.tvDateAndTime.text =
            details?.updated_at?.let { Common.formatTime(it) }
        activity?.let {
            Common.loadImageUsingURL(holder.imgProfilePic,
                otherMessageData.profile_img,
                it, true)
        }
    }

    class FansViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfilePic: CircleImageView = view.imgProfilePic
        val tvProfileName: TextView = view.tvProfileName
        val tvMessageContent: TextView = view.tvMessageContent
        val tvDateAndTime: TextView = view.tvDateAndTime
        val tvUnReadCount: TextView = view.tvUnReadCount
        val layOtherMessage: LinearLayout = view.layOtherMessage
    }

}


