package com.calendar.loyalfans.fragments.ppv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.PPVActivity
import com.calendar.loyalfans.adapter.OtherMessageAdapter
import com.calendar.loyalfans.model.response.OtherPPVData
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_ppv_message.*

class PPVOtherMessageFragment(
    private val otherPPVMessages: ArrayList<OtherPPVData>?,
) : Fragment() {

    companion object {
        fun newInstance(
            otherMessages: ArrayList<OtherPPVData>?,
        ): Fragment {
            return PPVOtherMessageFragment(otherMessages)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_ppv_message, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (otherPPVMessages != null) {
            setUpOtherMessageAdapter(otherPPVMessages)
        }
    }

    private fun setUpOtherMessageAdapter(myPpvMessages: ArrayList<OtherPPVData>) {
        Common.setupVerticalRecyclerView(rvPPVMessage, activity)
        val otherMessageAdapter = OtherMessageAdapter(myPpvMessages, activity)
        rvPPVMessage.adapter = otherMessageAdapter
        otherMessageAdapter.onOtherMessageAction =
            object : OtherMessageAdapter.OnOtherMessageAction {
                override fun otherMessage(otherPPVData: OtherPPVData) {
                    (activity as PPVActivity).loadFragment(25,otherPPVData)
                }
            }
    }

}