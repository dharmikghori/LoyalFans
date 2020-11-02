package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.PostData
import com.calendar.loyalfans.utils.Common
import com.google.android.material.tabs.TabLayout
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_home_post.view.*
import kotlinx.android.synthetic.main.layout_like_comment_bottom_view.view.*
import kotlinx.android.synthetic.main.layout_post_message_and_photos.view.*
import kotlinx.android.synthetic.main.layout_post_top_view.view.*
import java.util.*


class MyProfilePostAdapter(
    private var postListData: ArrayList<PostData>,
    private val activity: FragmentActivity?,
    private val isMyPost: Boolean = false,
    private val isProfileVisible: Boolean,
) :
    RecyclerView.Adapter<MyProfilePostAdapter.HomePostViewHolder>() {
    var onPostAction: OnPostAction? = null


    interface OnPostAction {
        fun onBottomReached(position: Int)
        fun onLikeUnLike(postData: PostData, position: Int)
        fun onComment(postData: PostData, position: Int)
        fun onBookmark(postData: PostData, position: Int)
        fun onEditPost(postData: PostData, position: Int)
        fun onDeletePost(postData: PostData, position: Int)
        fun onReportAbuse(postData: PostData, position: Int)
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
        if (isProfileVisible) {
            holder.imgPostVisible.visibility = View.GONE
        } else {
            holder.imgPostVisible.visibility = View.VISIBLE
        }
        holder.btnSendTip.setOnClickListener {
            activity?.let {
                Common.showSendDialog(it,
                    postData)
            }
        }
        holder.tvActivityMessage.text = postData.content
        holder.tvProfileName.text = postData.display_name
        holder.tvUserName.text = "@" + postData.username
        holder.tvTotalComment.text = postData.comments + " Comments"
        holder.tvTotalLike.text = postData.likes + " Likes"
        activity?.let {

            when {
                postData.images.isNotEmpty() -> {
                    holder.photos_viewpager.adapter =
                        PostImageVideoPagerAdapter(it, postData.images)
                    if (postData.images.size > 1) {
                        holder.tabLayout.setupWithViewPager(holder.photos_viewpager)
//                        holder.tabLayout.visibility = View.VISIBLE
                    } else {
//                        holder.tabLayout.visibility = View.VISIBLE
                    }
                }
//                postData.videos.isNotEmpty() -> {
//                    holder.photos_viewpager.adapter =
//                        PostImageVideoPagerAdapter(it, postData.videos)
//                    if (postData.videos.size > 1) {
//                        holder.tabLayout.setupWithViewPager(holder.photos_viewpager)
//                        holder.tabLayout.visibility = View.VISIBLE
//                    }                }
                else -> {
                    holder.tabLayout.visibility = View.GONE
                }
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

        holder.layComment.setOnClickListener {
            onPostAction?.onComment(postData, position)
        }
        holder.imgMoreOption.visibility = View.VISIBLE
        if (isMyPost) {
            holder.btnSendTip.visibility = View.GONE
        } else {
            holder.btnSendTip.visibility = View.VISIBLE
        }
        holder.imgMoreOption.setOnClickListener {
            openOptionMenu(it, postData, position)
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
        val imgMoreOption: ImageView = view.imgMoreOption
        val layComment: LinearLayout = view.layComment
        val imgPostVisible: ImageView = view.imgPostVisible
    }

    private fun openOptionMenu(view: View, postData: PostData, position: Int) {
        val activityActionMenu = PopupMenu(activity, view)
        val inflater = activityActionMenu.menuInflater
        activityActionMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.edit_post -> {
                    onPostAction?.onEditPost(postData, position)
                    return@setOnMenuItemClickListener true
                }
                R.id.delete_post -> {
                    onPostAction?.onDeletePost(postData, position)
                    return@setOnMenuItemClickListener true
                }
                R.id.report_post -> {
                    onPostAction?.onReportAbuse(postData, position)
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
        inflater.inflate(R.menu.post_option, activityActionMenu.menu)
        if (isMyPost) {
            activityActionMenu.menu.findItem(R.id.edit_post).isVisible = true
            activityActionMenu.menu.findItem(R.id.delete_post).isVisible = true
            activityActionMenu.menu.findItem(R.id.report_post).isVisible = false
        } else {
            activityActionMenu.menu.findItem(R.id.edit_post).isVisible = false
            activityActionMenu.menu.findItem(R.id.delete_post).isVisible = false
            activityActionMenu.menu.findItem(R.id.report_post).isVisible = true
        }
        activityActionMenu.show()
    }

}


