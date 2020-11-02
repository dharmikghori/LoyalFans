package com.calendar.loyalfans.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.transition.Slide
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.request.ReportRequest
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.report_post_dialog.*

class ReportPostDialog(context: AppCompatActivity, val otherID: String, val reportType: String) :
    Dialog(context) {
    private var onReportPost: OnReportPost? = null
    val activity: AppCompatActivity = context
    fun setOnPPVSend(onOptionSelection: OnReportPost?) {
        this.onReportPost = onOptionSelection
    }

    private fun dialogInit() {
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        val container = LinearLayout(context)
        container.layoutParams = params
        container.setBackgroundColor(Color.WHITE)
        container.orientation = LinearLayout.VERTICAL
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.report_post_dialog)
        setCancelable(true)
        val slide = Slide()
        slide.slideEdge = Gravity.START
        slide.duration = 3000
        slide.interpolator = AccelerateDecelerateInterpolator()
        setCanceledOnTouchOutside(true)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        window!!.attributes.windowAnimations = R.style.DialogAnimation
        tvReport.setOnClickListener {
            if (etReportComment.text.toString().isEmpty()) {
                Common.showToast(activity,context.getString(R.string.report_reason_validation))
            } else {
                val reportRequest =
                    ReportRequest(otherID, reportType, etReportComment.text.toString())
                onReportPost?.onReport(reportRequest)
            }
        }
    }

    interface OnReportPost {
        fun onReport(reportRequest: ReportRequest)
    }

    init {
        dialogInit()
    }
}