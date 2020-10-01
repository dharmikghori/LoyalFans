package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.model.request.CommentRequest
import com.calendar.loyalfans.model.response.CommentData
import com.calendar.loyalfans.utils.Common
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_comments.view.*
import java.util.*

class CommentAdapter(
    private var commentsList: ArrayList<CommentData>,
    private val activity: FragmentActivity?,
    private val isReplyComment: Boolean = false,
    private val postId: String,
) :
    RecyclerView.Adapter<CommentAdapter.FansViewHolder>() {
    var onCommentLikeUnLike: OnCommentLikeUnLike? = null

    interface OnCommentLikeUnLike {
        fun onLikeUnLikeComment(commentRequest: CommentRequest)
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }

    private fun getItem(position: Int): CommentData {
        return commentsList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FansViewHolder {
        return FansViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_comments,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FansViewHolder, position: Int) {
        val commentData = getItem(position)
        holder.tvProfileName.text = commentData.display_name
        holder.tvCommentContent.text = commentData.comment
        holder.tvCommentLikeCount.text = commentData.likes
        holder.cbCommentLike.setOnCheckedChangeListener(null)
        holder.cbCommentLike.isChecked = commentData.is_like == "1"
        holder.tvTimeAgo.text = commentData.created_at
        holder.cbCommentLike.setOnCheckedChangeListener { buttonView, isChecked ->
            val commentRequest = CommentRequest()
            commentRequest.user_id = Common.getUserId()
            commentRequest.post_id = postId
            commentRequest.type = when (isReplyComment) {
                true -> {
                    "reply"
                }
                false -> {
                    "comment"
                }
            }
            commentRequest.comment_id = commentData.id
            onCommentLikeUnLike?.onLikeUnLikeComment(commentRequest)
            var totalLikes: Int = commentData.likes.toInt()
            var likeStatus = 0
            when (isChecked) {
                true -> {
                    likeStatus = 1
                    totalLikes += 1
                }
                false -> {
                    likeStatus = 0
                    totalLikes -= 1
                }
            }
            commentData.likes = totalLikes.toString()
            commentData.is_like = likeStatus.toString()
            commentsList[position] = commentData
            notifyItemChanged(position)
        }
        activity?.let {
            Common.loadImageUsingURL(holder.imgProfilePic,
                commentData.profile_img,
                it)
        }
        if (!commentData.replay.isNullOrEmpty()) {
            holder.rvReplyComment.visibility = View.VISIBLE
            Common.setupVerticalRecyclerView(holder.rvReplyComment, activity)
            val commentAdapter = CommentAdapter(commentData.replay!!, activity, true, postId)
            holder.rvReplyComment.adapter = commentAdapter
            commentAdapter.onCommentLikeUnLike = object : OnCommentLikeUnLike {
                override fun onLikeUnLikeComment(commentRequest: CommentRequest) {
                    onCommentLikeUnLike?.onLikeUnLikeComment(commentRequest)
                }
            }
        } else {
            holder.rvReplyComment.visibility = View.GONE
        }
        holder.tvReply.setOnClickListener {
            if (activity != null) {
                if (holder.tvReply.text == activity.getString(R.string.cancel_reply)) {
                    BaseActivity.checkReplyOrNormalComment.value = ""
                    holder.tvReply.text = activity.getString(R.string.reply)
                    holder.tvReply.setTextColor(activity.resources.getColor(R.color.theme_black))
                } else {
                    BaseActivity.checkReplyOrNormalComment.value =
                        "${commentData.id},${commentData.display_name}"
                    holder.tvReply.text = activity.getString(R.string.cancel_reply)
                    holder.tvReply.setTextColor(activity.resources.getColor(R.color.red))
                }
            }
        }
    }

    class FansViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfilePic: CircleImageView = view.imgProfilePic
        val tvProfileName: TextView = view.tvProfileName
        val tvCommentContent: TextView = view.tvCommentContent
        val tvTimeAgo: TextView = view.tvTimeAgo
        val tvReply: TextView = view.tvReply
        val tvCommentLikeCount: TextView = view.tvCommentLikeCount
        val cbCommentLike: CheckBox = view.cbCommentLike
        val rvReplyComment: RecyclerView = view.rvReplyComment
    }

}


