package com.calendar.loyalfans.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.ViewPostImagesAndVideoWebView
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.RequestParams


class PostImageVideoPagerAdapter(
    private val context: Context,
    images: ArrayList<String>,
) :
    PagerAdapter() {

    var onPPVPost: OnPayPPVPost? = null

    interface OnPayPPVPost {
        fun onPostSeen()
    }

    private val imagesList = images
    override fun getCount(): Int {
        return imagesList.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        val viewPager = container as ViewPager
        val view = obj as View
        viewPager.removeView(view)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageData = imagesList[position]
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View =
            inflater.inflate(R.layout.fragment_post_image_video_pager, container, false)
        val imgPostImageVideoPager = view.findViewById<ImageView>(R.id.imgPostImageVideoPager)
        val imgPlayVideo = view.findViewById<ImageView>(R.id.imgPlayVideo)
        val imgMultipleImage = view.findViewById<ImageView>(R.id.imgMultipleImage)
        if (imageData.isEmpty() || imageData.length == 1) {
            imgMultipleImage.visibility = View.GONE
        } else {
            imgMultipleImage.visibility = View.VISIBLE
        }
        Common.loadImageUsingURL(imgPostImageVideoPager,
            imageData,
            context, true)
        val viewPager = container as ViewPager
        viewPager.addView(view, 0)
        if (Common.isVideo(imageData)) {
            imgPlayVideo.visibility = View.VISIBLE
        } else {
            imgPlayVideo.visibility = View.GONE
        }
        imgPostImageVideoPager.setOnClickListener {
            onPPVPost?.onPostSeen()
            context.startActivity(Intent(context,
                ViewPostImagesAndVideoWebView::class.java).putExtra(
                RequestParams.WEBVIEW_URL,
                imagesList))
        }
        return view
    }
}