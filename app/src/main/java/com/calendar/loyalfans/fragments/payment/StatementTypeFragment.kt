package com.calendar.loyalfans.fragments.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.adapter.StatementsEarningAdapter
import com.calendar.loyalfans.model.request.NotificationSecurityRequest
import com.calendar.loyalfans.model.response.StatementData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_statement_list.*
import java.util.*

class StatementTypeFragment(private val statementType: String) : Fragment() {

    companion object {
        fun newInstance(statementType: String): Fragment {
            return StatementTypeFragment(statementType)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_statement_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getStatementsByType(statementType)
    }

    private fun getStatementsByType(statementType: String) {
        val notificationSecurityRequest =
            NotificationSecurityRequest(statementType)
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.getStatement(
            notificationSecurityRequest,
            true
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        setUpStatementAdapter(it.data)
                        BaseActivity.currentBalance.value = it.balance
                    }
                })
    }

    private fun setUpStatementAdapter(data: ArrayList<StatementData>) {
        Common.setupVerticalRecyclerView(rvStatements, activity)
        rvStatements.adapter = StatementsEarningAdapter(data, activity)
    }

}