package com.calendar.loyalfans.fragments.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.request.NotificationSecurityRequest
import com.calendar.loyalfans.model.response.NotificationSecurityData
import com.calendar.loyalfans.retrofit.BaseViewModel
import kotlinx.android.synthetic.main.fragment_notification_setting.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.tvToolBarName

class NotificationSettingFragment : Fragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    companion object {
        fun newInstance() = NotificationSettingFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_notification_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.notifications)
        imgBack.setOnClickListener(this)
        getUpdatedNotificationData("")
        manageCheckListener(true)
    }

    private fun getUpdatedNotificationData(type: String) {
        val notificationSecurityRequest =
            NotificationSecurityRequest(type)
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.notificationSetting(
            notificationSecurityRequest, true
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        setUpNotificationData(it.data)
                    }
                })
    }

    private fun setUpNotificationData(data: NotificationSecurityData) {
        manageCheckListener()
        switchNotificationEmail.isChecked = data.noti_email == "1"
        cbNewComment.isChecked = data.site_noti_comm == "1"
        cbNewLike.isChecked = data.site_noti_likes == "1"
        cbNewTips.isChecked = data.site_noti_tips == "1"
        manageCheckListener(true)
    }

    private fun manageCheckListener(isListenerAdd: Boolean = false) {
        if (isListenerAdd) {
            cbNewComment.setOnCheckedChangeListener(this)
            cbNewLike.setOnCheckedChangeListener(this)
            cbNewLike.setOnCheckedChangeListener(this)
            switchNotificationEmail.setOnCheckedChangeListener(this)
        } else {
            cbNewComment.setOnCheckedChangeListener(null)
            cbNewLike.setOnCheckedChangeListener(null)
            cbNewLike.setOnCheckedChangeListener(null)
            switchNotificationEmail.setOnCheckedChangeListener(null)
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.imgBack -> {
                    activity?.onBackPressed()
                }
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView != null) {
            when (buttonView.id) {
                R.id.cbNewComment -> {
                    getUpdatedNotificationData("site_noti_comm")
                }
                R.id.cbNewLike -> {
                    getUpdatedNotificationData("site_noti_likes")
                }
                R.id.cbNewTips -> {
                    getUpdatedNotificationData("site_noti_tips ")
                }
                else -> {
                    getUpdatedNotificationData("noti_email")
                }
            }

        }
    }

}