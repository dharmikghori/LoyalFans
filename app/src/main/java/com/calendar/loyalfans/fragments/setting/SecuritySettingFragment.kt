package com.calendar.loyalfans.fragments.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.request.NotificationSecurityRequest
import com.calendar.loyalfans.model.response.NotificationSecurityData
import com.calendar.loyalfans.retrofit.BaseViewModel
import kotlinx.android.synthetic.main.fragment_security_setting.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.tvToolBarName

class SecuritySettingFragment : Fragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    companion object {
        fun newInstance() = SecuritySettingFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_security_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.security)
        imgBack.setOnClickListener(this)
        getUpdatedSecurityData("")
        manageCheckListener(true)
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

    private fun getUpdatedSecurityData(type: String, subType: String = "") {
        val notificationSecurityRequest =
            NotificationSecurityRequest(type, subType)
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.notificationSetting(
            notificationSecurityRequest, true
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        setUpSecurityData(it.data)
                    }
                })
    }


    private fun setUpSecurityData(data: NotificationSecurityData) {
        manageCheckListener()
        switchActivityStatus.isChecked = data.activity_status == "1"
        switchSubsOffer.isChecked = data.sub_offers == "1"
        when (data.site_noti_about) {
            "0" -> {
                rbNobody.isChecked = true
            }
            "1" -> {
                rbMutualFriend.isChecked = true
            }
            "2" -> {
                rbSubscribe.isChecked = true
            }
        }
        manageCheckListener(true)
    }

    private fun manageCheckListener(isListenerAdd: Boolean = false) {
        if (isListenerAdd) {
            switchActivityStatus.setOnCheckedChangeListener(this)
            switchSubsOffer.setOnCheckedChangeListener(this)
            rbSecurity.setOnCheckedChangeListener(this)
        } else {
            switchActivityStatus.setOnCheckedChangeListener(null)
            switchSubsOffer.setOnCheckedChangeListener(null)
            rbSecurity.setOnCheckedChangeListener(null)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView != null) {
            when (buttonView.id) {
                R.id.switchActivityStatus -> {
                    getUpdatedSecurityData("activity_status")
                }
                R.id.switchSubsOffer -> {
                    getUpdatedSecurityData("sub_offers")
                }
                R.id.rbSecurity -> {
                    getUpdatedSecurityData("site_noti_about")
                }
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rbNobody -> {
                getUpdatedSecurityData("site_noti_about", "0")
            }
            R.id.rbMutualFriend -> {
                getUpdatedSecurityData("site_noti_about", "1")
            }
            R.id.rbSubscribe -> {
                getUpdatedSecurityData("site_noti_about", "2")
            }
        }
    }
}