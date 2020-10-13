package com.calendar.loyalfans.fragments.ppv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.PPVActivity
import com.calendar.loyalfans.adapter.MyMessagePPVAdapter
import com.calendar.loyalfans.model.response.MyPPVData
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_my_message_ppv.*

class PPVMyMessageFragment(
    private val myPpvMessages: ArrayList<MyPPVData>?,
) : Fragment() {

    companion object {
        fun newInstance(
            myPpvMessages: ArrayList<MyPPVData>?,
        ): Fragment {
            return PPVMyMessageFragment(myPpvMessages)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_my_message_ppv, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (myPpvMessages != null) {
            setUpMyMessageAdapter(myPpvMessages)
        }
    }

    private fun setUpMyMessageAdapter(myPpvMessages: ArrayList<MyPPVData>?) {
        if (myPpvMessages != null) {
            Common.setupVerticalRecyclerView(rvMyPPVMessage, activity)
            val myMessagePPVAdapter = MyMessagePPVAdapter(myPpvMessages, activity)
            rvMyPPVMessage.adapter = myMessagePPVAdapter
            myMessagePPVAdapter.myMessageAction = object : MyMessagePPVAdapter.MyMessageAction {
                override fun onAnalytics(myPPVData: MyPPVData) {
                    (activity as PPVActivity).loadFragment(PPVAnaliticsFragment.newInstance(
                        myPPVData),
                        Common.getTagBasedOnType(26))
                }
            }
        }
    }


}