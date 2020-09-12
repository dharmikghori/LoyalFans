package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.PostData
import com.calendar.loyalfans.utils.Common
import com.google.android.material.tabs.TabLayout
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_like_comment_bottom_view.view.*
import kotlinx.android.synthetic.main.layout_post_message_and_photos.view.*
import kotlinx.android.synthetic.main.layout_post_top_view.view.*
import java.util.*


class HomePostAdapter(
    private var postListData: ArrayList<PostData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<HomePostAdapter.HomePostViewHolder>() {
    var onPostAction: OnPostAction? = null

    interface OnPostAction {
        fun onBottomReached(position: Int)
        fun onLikeUnLike(postData: PostData, position: Int)
        fun onComment(postData: PostData, position: Int)
        fun onBookmark(postData: PostData, position: Int)
    }

    override fun getItemCount(): Int {
        return postListData.size
    }


    private fun getItem(position: Int): PostData {
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
        holder.tvTotalComment.text = postData.comments + " Comments"
        holder.tvTotalLike.text = postData.likes + " Likes"
        activity?.let {
            holder.photos_viewpager.adapter = PostImageVideoPagerAdapter(it, postData.images)
            if (postData.images.size > 1) {
                holder.tabLayout.setupWithViewPager(holder.photos_viewpager)
                holder.tabLayout.visibility = View.VISIBLE
            } else {
                holder.tabLayout.visibility = View.GONE
            }
        }

        activity?.let {
            Common.loadImageUsingURL(holder.imgProfilePic,
                postData.profile_img, it, true)
        }
        holder.cbLikeUnlike.setOnCheckedChangeListener(null)
        holder.cbLikeUnlike.isChecked = when (postData.is_likes) {
            "0" -> false
            "1" -> true
            else ->
                false
        }
        holder.cbBookmark.isChecked = when (postData.bookmark) {
            "0" -> false
            "1" -> true
            else ->
                false
        }
        holder.cbLikeUnlike.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            var likesCount = Integer.parseInt(postData.likes)
            var isLikeStatus = postData.is_likes
            when (b) {
                true -> {
                    likesCount += 1
                    isLikeStatus = "1"
                }
                false -> {
                    likesCount -= 1
                    isLikeStatus = "0"
                }
            }
            postData.likes = likesCount.toString()
            postData.is_likes = isLikeStatus
            onPostAction?.onLikeUnLike(postData, position)
            notifyDataSetChanged()
        }
        if (position == postListData.size - 1) {
            onPostAction?.onBottomReached(position)
        }
    }

    class HomePostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnSendTip: Button = view.btnSendTip
        val imgProfilePic: CircleImageView = view.imgProfilePic
        val tvProfileName: TextView = view.tvProfileName
        val tvUserName: TextView = view.tvUserName
        val tvActivityMessage: TextView = view.tvActivityMessage
        val cbLikeUnlike: CheckBox = view.cbLikeUnlike
        val tvTotalLike: TextView = view.tvTotalLike
        val tvTotalComment: TextView = view.tvTotalComment
        val cbBookmark: CheckBox = view.cbBookmark
        val tabLayout: TabLayout = view.tabLayout
        val photos_viewpager: ViewPager = view.photos_viewpager
    }


}


