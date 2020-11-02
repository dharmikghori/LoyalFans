package com.calendar.loyalfans.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.OtherProfileActivity
import com.calendar.loyalfans.model.response.FansData
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.RequestParams
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_fans_list.view.*
import java.util.*

class FansListAdapter(
    private var fansList: ArrayList<FansData>,
    private val activity: FragmentActivity?,
    private val fansType: String = "",
) :
    RecyclerView.Adapter<FansListAdapter.FansViewHolder>() {

    fun updateFansList(fansList: ArrayList<FansData>) {
        this.fansList = fansList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return fansList.size
    }

    private fun getItem(position: Int): FansData {
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
        holder.tvUserName.text = "@" + fansData.username
        if (fansData.end_date != null) {
            holder.tvExpiryDate.text = "Till " + Common.formatDate(fansData.end_date!!)
        }
        if (fansType == "2" || fansType == "3" || fansType == "0") {
            holder.imgFansMoreOption.visibility = View.VISIBLE
            holder.imgFansMoreOption.setOnClickListener {
                onBlockMoreOption(fansData,
                    holder.imgFansMoreOption, position)
            }
        } else {
            holder.imgFansMoreOption.visibility = View.GONE
        }
        holder.tvExpiryDate.isSelected = true
        activity?.let { Common.loadImageUsingURL(holder.imgProfilePic, fansData.profile_img, it) }
        holder.layProfile.setOnClickListener {
            activity?.startActivity(Intent(activity, OtherProfileActivity::class.java).putExtra(
                RequestParams.PROFILE_ID,
                fansData.fanid))
        }
    }

    var onFansOptions: OnFansOptions? = null


    interface OnFansOptions {
        fun onFansBlock(fansData: FansData, position: Int)
        fun onFansUnBlock(fansData: FansData, position: Int)
    }

    private fun onBlockMoreOption(fansData: FansData, imgFansMoreOption: View, position: Int) {
        val activityActionMenu = PopupMenu(activity, imgFansMoreOption)
        val inflater = activityActionMenu.menuInflater
        activityActionMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.block_fans -> {
                    onFansOptions?.onFansBlock(fansData, position)
                    return@setOnMenuItemClickListener true
                }
                R.id.unblock_fans -> {
                    onFansOptions?.onFansUnBlock(fansData, position)
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
        inflater.inflate(R.menu.fans_block_option, activityActionMenu.menu)
        if (fansData.block == "0") {
            activityActionMenu.menu.findItem(R.id.block_fans).isVisible = true
            activityActionMenu.menu.findItem(R.id.unblock_fans).isVisible = false
        } else if (fansData.block == "1") {
            activityActionMenu.menu.findItem(R.id.block_fans).isVisible = false
            activityActionMenu.menu.findItem(R.id.unblock_fans).isVisible = true
        }
        activityActionMenu.show()
    }

    class FansViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfilePic: CircleImageView = view.imgProfilePic
        val tvProfileName: TextView = view.tvProfileName
        val tvUserName: TextView = view.tvUserName
        val tvExpiryDate: TextView = view.tvExpiryDate
        val layProfile: LinearLayout = view.layProfile
        val imgFansMoreOption: ImageView = view.imgFansMoreOption

    }

}


