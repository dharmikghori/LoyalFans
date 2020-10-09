package com.calendar.loyalfans.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.calendar.loyalfans.R


class PostImageVideoPagerWebViewAdapter(
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
        val imageData = imagesList[position]
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View =
            inflater.inflate(R.layout.fragment_post_image_video_pager_webview, container, false)
        val webview = view.findViewById<WebView>(R.id.webViewPhotoVideos)
        val settings: WebSettings = webview.settings
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.loadsImagesAutomatically = true
        settings.javaScriptEnabled = true
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.mediaPlaybackRequiresUserGesture = false
        settings.javaScriptCanOpenWindowsAutomatically = true
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webview.loadUrl(imageData)
        val viewPager = container as ViewPager
        viewPager.addView(view, 0)
        return view
    }
}