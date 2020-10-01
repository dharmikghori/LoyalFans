package com.calendar.loyalfans.fragments.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.calendar.loyalfans.R
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.tvToolBarName

class AddCardFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = AddCardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_addcard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.add_card)
        imgBack.setOnClickListener(this)
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


}