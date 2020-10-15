package com.calendar.loyalfans.activities


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.PostImageVideoPagerWebViewAdapter
import com.calendar.loyalfans.utils.RequestParams
import kotlinx.android.synthetic.main.activity_multiple_view_webview.*

open class ViewPostImagesAndVideoWebView : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_multiple_view_webview)
        val webViewURLs = intent.getStringArrayListExtra(RequestParams.WEBVIEW_URL)
//        val webViewURLs = ArrayList<String>()
//        webViewURLs.add("https://www.loyalfansonly.com/assets/upload/post/video/SampleVideo_360x240_1mb.mp4")
//        webViewURLs.add("https://www.loyalfansonly.com/assets/upload/post/image/post_1600671719_file.jpg")
//        webViewURLs.add("https://www.loyalfansonly.com/assets/upload/post/image/post_1600671719_file.jpg")
        imgBack.setOnClickListener(this)
        if (webViewURLs != null) {
            setUpWebView(webViewURLs)
            if (webViewURLs.size > 1) {
                previewTabLayout.setupWithViewPager(webViewViewPager)
                previewTabLayout.visibility = View.VISIBLE
//                imgPrevWebView.visibility = View.VISIBLE
//                imgNextWebView.setOnClickListener(this)
//                imgPrevWebView.setOnClickListener(this)
            } else {
                previewTabLayout.visibility = View.GONE
//                imgPrevWebView.visibility = View.GONE
            }

//            webViewViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//                override fun onPageScrolled(
//                    position: Int,
//                    positionOffset: Float,
//                    positionOffsetPixels: Int,
//                ) {
//
//                }
//
//                override fun onPageSelected(position: Int) {
//                    val postImageVideoPagerWebViewAdapter =
//                        webViewViewPager.adapter as PostImageVideoPagerWebViewAdapter
//                    val allViews = postImageVideoPagerWebViewAdapter.allViews
//                    for (i in 0 until allViews.size) {
//                        val webViewData = allViews[i]
//                        if (i == position) {
//                            webViewData.onResume()
//                        } else {
//                            webViewData.onPause()
//                        }
//                        postImageVideoPagerWebViewAdapter.notifyDataSetChanged()
//                    }
//                }
//
//                override fun onPageScrollStateChanged(state: Int) {
//                }
//            })

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView(webViewURLs: ArrayList<String>) {
        webViewViewPager.adapter = PostImageVideoPagerWebViewAdapter(this, webViewURLs)
        webViewViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
            }

            override fun onPageSelected(position: Int) {
                (webViewViewPager.adapter as PostImageVideoPagerWebViewAdapter).notifyDataSetChanged()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.imgBack -> {
                    onBackPressed()
                }
                R.id.imgPrevWebView -> {
                    val currentValue = webViewViewPager.currentItem - 1
                    webViewViewPager.currentItem = currentValue
                }
                R.id.imgNextWebView -> {
                    val currentValue = webViewViewPager.currentItem + 1
                    webViewViewPager.currentItem = currentValue
                }
            }
        }
    }
}


