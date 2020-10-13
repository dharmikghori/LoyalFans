package com.calendar.loyalfans.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.MyPPVData
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.layout_child_view.view.*
import java.util.*

class MyMessagePPVAdapter(
    private var fansList: ArrayList<MyPPVData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<MyMessagePPVAdapter.FansViewHolder>() {
    private var lastClickedPosition = -1


    interface MyMessageAction {
        fun onAnalytics(myPPVData: MyPPVData)
    }

    var myMessageAction: MyMessageAction? = null

    override fun getItemCount(): Int {
        return fansList.size
    }

    private fun getItem(position: Int): com.calendar.loyalfans.model.response.MyPPVData {
        return fansList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FansViewHolder {
        return FansViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_child_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FansViewHolder, position: Int) {
        val otherMessageData = getItem(position)
        holder.tvGroupTitle.text = Common.formatDate(otherMessageData.created_at)
        if (activity != null) {
            holder.tvAllSubscriberAmount.text =
                activity.getString(R.string.dollar) + otherMessageData.details?.price
            holder.tvMessageContent.text = otherMessageData.details?.content
            val files = otherMessageData.details?.files
            if (files != null) {
                if (!files.isNullOrEmpty()) {
                    holder.imgFile.visibility = View.VISIBLE
                    Common.loadImageUsingURL(holder.imgFile,
                        otherMessageData.details!!.files[0].file_name, activity)
                } else {
                    holder.imgFile.visibility = View.GONE
                }
            } else {
                holder.imgFile.visibility = View.GONE
            }
            holder.tvTotalPurchasesAmount.text =
                activity.getString(R.string.dollar) + otherMessageData.analytics?.earning?.earning
            holder.tvSeenCount.text = otherMessageData.analytics?.earning?.seen

        }
        holder.tvGroupTitle.setOnClickListener {
            lastClickedPosition = if (lastClickedPosition == position) {
                -1
            } else {
                position
            }
            notifyDataSetChanged()
        }
        holder.btnAnalytics.setOnClickListener {
            myMessageAction?.onAnalytics(otherMessageData)
        }
        if (lastClickedPosition == position) {
            holder.layMessageVisibleGone.animate()
                .translationY(0F)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        holder.layMessageVisibleGone.visibility = View.VISIBLE
                    }
                })
        } else {
            holder.layMessageVisibleGone.animate()
                .translationY(0F)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        holder.layMessageVisibleGone.visibility = View.GONE
                    }
                })
        }
    }

    class FansViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layMessageVisibleGone: LinearLayout = view.layMessageVisibleGone
        val tvGroupTitle: TextView = view.tvGroupTitle
        val tvAllSubscriberAmount: TextView = view.tvAllSubscriberAmount
        val tvMessageContent: TextView = view.tvMessageContent
        val imgFile: ImageView = view.imgFile
        val tvTotalPurchasesAmount: TextView = view.tvTotalPurchasesAmount
        val tvSeenCount: TextView = view.tvSeenCount
        val btnAnalytics: Button = view.btnAnalytics
    }
}


