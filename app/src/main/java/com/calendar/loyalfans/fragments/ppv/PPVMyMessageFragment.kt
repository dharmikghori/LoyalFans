package com.calendar.loyalfans.fragments.ppv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.MyMessagePPVAdapter
import com.calendar.loyalfans.model.response.MyPPVData
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_my_message_ppv.*
import java.util.*
import kotlin.collections.ArrayList

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
            Common.setupVerticalRecyclerView(rvMyPPVMessage,activity)
            rvMyPPVMessage.adapter = activity?.let { MyMessagePPVAdapter(myPpvMessages, it) }
        }
    }


}