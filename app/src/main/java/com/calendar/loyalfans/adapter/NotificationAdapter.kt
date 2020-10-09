package com.calendar.loyalfans.adapter

import android.content.Intent
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.OtherProfileActivity
import com.calendar.loyalfans.model.response.NotificationData
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.RequestParams
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_fans_list.view.imgProfilePic
import kotlinx.android.synthetic.main.layout_fans_list.view.tvProfileName
import kotlinx.android.synthetic.main.layout_notification.view.*
import java.util.*

class NotificationAdapter(
    private var notificationList: ArrayList<NotificationData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<NotificationAdapter.FansViewHolder>() {


    override fun getItemCount(): Int {
        return notificationList.size
    }

    private fun getItem(position: Int): NotificationData {
        return notificationList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FansViewHolder {
        return FansViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_notification,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FansViewHolder, position: Int) {
        val notificationData = getItem(position)
        setUpClickHere(holder.tvProfileName,
            "<strong><a href='clickhere' style='text-decoration:none'>${notificationData.user_display_name}</a></strong> ${notificationData.text}",
            notificationData)
        holder.tvNotificationTime.text = notificationData.created_at
        activity?.let {
            Common.loadImageUsingURL(holder.imgProfilePic,
                notificationData.user_profile,
                it)
            Common.loadImageUsingURL(holder.imgPost,
                notificationData.post_img,
                it)
        }
    }

    private fun setUpClickHere(
        textView: TextView,
        textForTextView: String,
        notificationData: NotificationData,
    ) {
        val sequence: CharSequence =
            Html.fromHtml(textForTextView)
        val strBuilder = SpannableStringBuilder(sequence)
        val urls: Array<URLSpan> = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {
            makeLinkClickable(strBuilder, span, notificationData)
        }
        textView.text = strBuilder
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun makeLinkClickable(
        strBuilder: SpannableStringBuilder,
        span: URLSpan,
        notificationData: NotificationData,
    ) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                activity?.startActivity(Intent(activity, OtherProfileActivity::class.java)
                    .putExtra(RequestParams.PROFILE_ID, notificationData.user_id))
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }


    class FansViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfilePic: CircleImageView = view.imgProfilePic
        val tvProfileName: TextView = view.tvProfileName
        val tvNotificationTime: TextView = view.tvNotificationTime
        val imgPost: ImageView = view.imgPost
    }

}


