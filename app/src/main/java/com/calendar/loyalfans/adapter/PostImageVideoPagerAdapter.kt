package com.calendar.loyalfans.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.calendar.loyalfans.R


class PostImageVideoPagerAdapter(
    private val context: Context,
    images: ArrayList<String>,
) :
    PagerAdapter() {
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
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View =
            inflater.inflate(R.layout.fragment_post_image_video_pager, container, false)
        val imgPostImageVideoPager = view.findViewById<ImageView>(R.id.imgPostImageVideoPager)
        com.calendar.loyalfans.utils.Common.loadImageUsingURL(imgPostImageVideoPager,
            imagesList[position],
            context, true)
        val viewPager = container as ViewPager
        viewPager.addView(view, 0)
        return view
    }
}