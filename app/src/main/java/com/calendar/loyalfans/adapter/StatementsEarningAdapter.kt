package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.layout_earning_statement.view.*
import java.util.*

class StatementsEarningAdapter(
    private var statementList: ArrayList<com.calendar.loyalfans.model.response.StatementData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<StatementsEarningAdapter.StatementViewHolder>() {


    override fun getItemCount(): Int {
        return statementList.size
    }

    private fun getItem(position: Int): com.calendar.loyalfans.model.response.StatementData {
        return statementList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatementViewHolder {
        return StatementViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_earning_statement,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StatementViewHolder, position: Int) {
        val statementItem = getItem(position)
        holder.tvStatementDate.text = Common.formatDate(statementItem.payment_date)
        holder.tvStatementType.text = statementItem.type
        holder.tvStatementAmount.text = statementItem.amount
    }


    class StatementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStatementDate: TextView = view.tvStatementDate
        val tvStatementType: TextView = view.tvStatementType
        val tvStatementAmount: TextView = view.tvStatementAmount
    }

}


