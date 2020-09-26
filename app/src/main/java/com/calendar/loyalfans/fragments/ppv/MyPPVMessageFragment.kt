package com.calendar.loyalfans.fragments.ppv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.OtherMessageAdapter
import com.calendar.loyalfans.model.response.OtherPPVData
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_favorite_list.*
import kotlinx.android.synthetic.main.fragment_ppv_message.*

class MyPPVMessageFragment(
    private val otherPPVMessages: ArrayList<OtherPPVData>?,
) : Fragment() {

    companion object {
        fun newInstance(
            otherMessages: ArrayList<OtherPPVData>?,
        ): Fragment {
            return MyPPVMessageFragment(otherMessages)
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

    private fun setUpOtherMessageAdapter(myPpvMessages: ArrayList<OtherPPVData>?) {
        Common.setupVerticalRecyclerView(rvPPVMessage, activity)
        rvPPVMessage.adapter = myPpvMessages?.let { OtherMessageAdapter(it, activity) }
    }

}