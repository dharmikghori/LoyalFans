package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.MyPPVDetailsData
import com.calendar.loyalfans.model.response.MyPPVFilesData
import com.calendar.loyalfans.utils.Common
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.layout_other_message_detail.view.*
import kotlinx.android.synthetic.main.layout_other_ppv_message_and_photos.view.*

class OtherDetailsMessageAdapter(
    private var otherMessageList: ArrayList<MyPPVDetailsData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<OtherDetailsMessageAdapter.FansViewHolder>() {

    var onPayPPV: OnPayPPVPost? = null

    interface OnPayPPVPost {
        fun onPay(otherPPVData: MyPPVDetailsData,position: Int)
    }

    override fun getItemCount(): Int {
        return otherMessageList.size
    }

    private fun getItem(position: Int): MyPPVDetailsData {
        return otherMessageList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FansViewHolder {
        return FansViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_other_message_detail,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FansViewHolder, position: Int) {
        val otherMessagePPVDetailsData = getItem(position)
        holder.tvPPVMessageDateTime.text = Common.formatDate(otherMessagePPVDetailsData.updated_at)
        holder.tvActivityMessage.text = otherMessagePPVDetailsData.content
        if (otherMessagePPVDetailsData.type == "PAID" && otherMessagePPVDetailsData.paid == "0") {
            holder.imgLock.visibility = View.VISIBLE
            holder.tapToPay.visibility = View.VISIBLE
            holder.tapToPay.text = "pay $" + otherMessagePPVDetailsData.price + " to view"
            holder.tapToPay.setOnClickListener {
                onPayPPV?.onPay(otherMessagePPVDetailsData,position)
            }
        } else {
            holder.imgLock.visibility = View.GONE
            holder.tapToPay.visibility = View.GONE
        }
        activity?.let {
            val imagesAndVideos: ArrayList<String> =
                getPhotosArray(otherMessagePPVDetailsData.files)
            holder.photos_viewpager.adapter = PostImageVideoPagerAdapter(it, imagesAndVideos)
            if (imagesAndVideos.size > 1) {
                holder.tabLayout.setupWithViewPager(holder.photos_viewpager)
                holder.tabLayout.visibility = View.VISIBLE
            } else {
                holder.tabLayout.visibility = View.GONE
            }
        }

    }

    private fun getPhotosArray(files: java.util.ArrayList<MyPPVFilesData>): java.util.ArrayList<String> {
        val allThumb: ArrayList<String> = ArrayList()
        for (fileData in files) {
            allThumb.add(fileData.file_name)
        }
        return allThumb
    }

    class FansViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPPVMessageDateTime: TextView = view.tvPPVMessageDateTime
        val tvActivityMessage: TextView = view.tvActivityMessage
        val photos_viewpager: ViewPager = view.photos_viewpager
        val imgLock: ImageView = view.imgLock
        val tabLayout: TabLayout = view.tabLayout
        val tapToPay: TextView = view.tapToPay
    }

}


