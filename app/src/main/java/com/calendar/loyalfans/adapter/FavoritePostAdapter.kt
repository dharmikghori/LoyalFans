package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.FavouriteData
import com.calendar.loyalfans.utils.Common
import com.google.android.material.tabs.TabLayout
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_home_post.view.*
import kotlinx.android.synthetic.main.layout_post_message_and_photos.view.*
import kotlinx.android.synthetic.main.layout_post_top_view.view.*
import java.util.*


class FavoritePostAdapter(
    private var postListData: ArrayList<FavouriteData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<FavoritePostAdapter.HomePostViewHolder>() {
    var onPostAction: OnPostAction? = null


    interface OnPostAction {
        fun onBottomReached(position: Int)
    }

    override fun getItemCount(): Int {
        return postListData.size
    }


    private fun getItem(position: Int): FavouriteData {
        return postListData[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePostViewHolder {
        return HomePostViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_home_post,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomePostViewHolder, position: Int) {
        val postData = getItem(position)
        holder.btnSendTip.setOnClickListener { activity?.let { Common.showSendDialog(it) } }
        holder.tvActivityMessage.text = postData.content
        holder.tvProfileName.text = postData.display_name
        holder.tvUserName.text = postData.username
        activity?.let {

            when {
                postData.images.isNotEmpty() -> {
                    holder.photos_viewpager.adapter =
                        PostImageVideoPagerAdapter(it, postData.images)
                    if (postData.images.size > 1) {
                        holder.tabLayout.setupWithViewPager(holder.photos_viewpager)
                        holder.tabLayout.visibility = View.VISIBLE
                    } else {
                        holder.tabLayout.visibility = View.VISIBLE
                    }
                }
                else -> {
                    holder.tabLayout.visibility = View.GONE
                }
            }
        }

        activity?.let {
            Common.loadImageUsingURL(holder.imgProfilePic,
                postData.profile_img, it, true)
        }
        if (position == postListData.size - 1) {
            onPostAction?.onBottomReached(position)
        }
        holder.imgMoreOption.visibility = View.GONE
        holder.btnSendTip.visibility = View.GONE
        holder.layLikeAndComment.visibility = View.GONE

    }

    class HomePostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnSendTip: Button = view.btnSendTip
        val imgProfilePic: CircleImageView = view.imgProfilePic
        val tvProfileName: TextView = view.tvProfileName
        val tvUserName: TextView = view.tvUserName
        val tvActivityMessage: TextView = view.tvActivityMessage
        val tabLayout: TabLayout = view.tabLayout
        val photos_viewpager: ViewPager = view.photos_viewpager
        val imgMoreOption: ImageView = view.imgMoreOption
        val layLikeAndComment: View = view.layLikeAndComment
    }
}


