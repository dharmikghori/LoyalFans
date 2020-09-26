package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.CommentData
import com.calendar.loyalfans.utils.Common
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_comments.view.*
import java.util.*

class CommentAdapter(
    private var commentsList: ArrayList<CommentData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<CommentAdapter.FansViewHolder>() {


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
        holder.cbCommentLike.isChecked = commentData.is_like == "1"
        activity?.let { Common.loadImageUsingURL(holder.imgProfilePic, commentData.profile_img, it) }
        holder.tvReply.setOnClickListener {}
    }

    class FansViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfilePic: CircleImageView = view.imgProfilePic
        val tvProfileName: TextView = view.tvProfileName
        val tvCommentContent: TextView = view.tvCommentContent
        val tvTimeAgo: TextView = view.tvTimeAgo
        val tvReply: TextView = view.tvReply
        val tvCommentLikeCount: TextView = view.tvCommentLikeCount
        val cbCommentLike: CheckBox = view.cbCommentLike
    }

}


