package com.calendar.loyalfans.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.FansSelectionAdapter
import com.calendar.loyalfans.model.request.FansFollowingRequest
import com.calendar.loyalfans.model.response.FansData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.layout_fans_selection_dialog.*

class FansSelectionDialog(context: AppCompatActivity) : Dialog(context) {
    private var onPPVSend: OnPPVSend? = null
    val activity: AppCompatActivity = context
    fun setOnPPVSend(onOptionSelection: OnPPVSend?) {
        this.onPPVSend = onOptionSelection
    }

    private var fansSelectionAdapter: FansSelectionAdapter? = null
    private fun dialogInit() {
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        val container = LinearLayout(context)
        container.layoutParams = params
        container.setBackgroundColor(Color.WHITE)
        container.orientation = LinearLayout.VERTICAL
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_fans_selection_dialog)
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
        getFans()
        imgSendPPV.setOnClickListener {
            if (fansSelectionAdapter != null) {
                val fans = fansSelectionAdapter!!.getFans()
                val filteredList = fans.filter { it.isSelected }
                if (filteredList.isNotEmpty()) {
                    onPPVSend?.onSendPPV(filteredList)
                    dismiss()
                } else {
                    Common.showToast(activity, "Please select at least one fan")
                }
            }
        }

        cbAllSubscriber.setOnCheckedChangeListener { buttonView, isChecked ->
            if (fansSelectionAdapter != null) {
                val fans = fansSelectionAdapter!!.getFans()
                val updatedFans = ArrayList<FansData>()
                for (fanData in fans) {
                    fanData.isSelected = isChecked
                    updatedFans.add(fanData)
                }
                fansSelectionAdapter!!.setFans(updatedFans)
            }
        }
        etSearchFans.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                fansSelectionAdapter?.filter?.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun getFans() {
        val fansFollowingRequest =
            FansFollowingRequest("0",
                Common.getUserId())
        val baseViewModel = ViewModelProvider(activity).get(BaseViewModel::class.java)
        baseViewModel.getFansByType(
            fansFollowingRequest,
            true
        )
            .observe(activity,
                {
                    if (it.status) {
                        setUpFansAdapter(it.data)
                    }
                    if (it.data.isNullOrEmpty()) {
                        Common.showToast(activity, context.getString(R.string.you_dont_fans))
                        dismiss()
                    }
                })
    }

    private fun setUpFansAdapter(data: ArrayList<FansData>) {
        Common.setupVerticalRecyclerView(rvFansForPPV, context)
        fansSelectionAdapter = FansSelectionAdapter(data, data, activity)
        rvFansForPPV.adapter = fansSelectionAdapter

    }

    interface OnPPVSend {
        fun onSendPPV(selectedFans: List<FansData>)
    }

    init {
        dialogInit()
    }
}