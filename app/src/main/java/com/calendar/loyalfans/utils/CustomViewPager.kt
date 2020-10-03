package com.calendar.loyalfans.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class CustomViewPager : ViewPager {

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    // update your onTouchEvent method
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }

    // update your onInterceptTouchEvent method
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return false
    }

}