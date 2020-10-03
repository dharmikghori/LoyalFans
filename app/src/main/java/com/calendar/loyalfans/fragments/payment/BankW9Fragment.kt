package com.calendar.loyalfans.fragments.payment

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.MainActivity
import com.calendar.loyalfans.activities.WebViewActivity
import com.calendar.loyalfans.retrofit.APIServices
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.RequestParams
import kotlinx.android.synthetic.main.fragment_w9.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.tvToolBarName


class BankW9Fragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = BankW9Fragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_w9, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.upload_w9)
        imgBack.setOnClickListener(this)
        setUpClickHere()
        checkStatusOfW9Form()
    }

    private fun checkStatusOfW9Form() {
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.w9FormStatus(true).observe(viewLifecycleOwner, {
            if (it.status) {
                layNoW9Form.visibility = View.GONE
            } else {
                layNoW9Form.visibility = View.VISIBLE
            }
        })
    }

    private fun setUpClickHere() {
        val sequence: CharSequence =
            Html.fromHtml("Please Fill out W9 Form First to be able to request a Payout. <a href='clickhere'>Click Here</a> to fill a W9 Form")
        val strBuilder = SpannableStringBuilder(sequence)
        val urls: Array<URLSpan> = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {
            makeLinkClickable(strBuilder, span)
        }
        tvClickHere.text = strBuilder
        tvClickHere.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(activity,
                    WebViewActivity::class.java).putExtra(RequestParams.WEBVIEW_URL,
                    APIServices.W9_FORM_WEB_URL + Common.getUserId()))
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.imgBack -> {
                    activity?.onBackPressed()
                }
                R.id.tvW9Form -> {
                    (activity as MainActivity).loadFragment(20)
                }
                R.id.tvBankTransfer -> {
                    (activity as MainActivity).loadFragment(21)
                }
            }
        }
    }


}