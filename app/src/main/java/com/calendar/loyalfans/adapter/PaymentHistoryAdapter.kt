package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.layout_payment_history.view.*
import java.util.*

class PaymentHistoryAdapter(
    private var statementList: ArrayList<com.calendar.loyalfans.model.response.StatementData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<PaymentHistoryAdapter.StatementViewHolder>() {


    override fun getItemCount(): Int {
        return statementList.size
    }

    private fun getItem(position: Int): com.calendar.loyalfans.model.response.StatementData {
        return statementList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatementViewHolder {
        return StatementViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_payment_history,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StatementViewHolder, position: Int) {
        val statementItem = getItem(position)
        holder.tvToPay.text = statementItem.display_name
        holder.tvPaymentType.text = statementItem.type
        holder.tvTransactionID.text = statementItem.txn_id
        holder.tvCardType.text = statementItem.brand
        holder.tvLast4Digit.text = statementItem.last4
        holder.tvAmount.text = "$ " + statementItem.amount
        holder.tvPaymentDate.text = Common.formatDate(statementItem.payment_date)
        holder.tvTransactionID.isSelected = true
    }


    class StatementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvToPay: TextView = view.tvToPay
        val tvPaymentType: TextView = view.tvPaymentType
        val tvTransactionID: TextView = view.tvTransactionID
        val tvCardType: TextView = view.tvCardType
        val tvLast4Digit: TextView = view.tvLast4Digit
        val tvAmount: TextView = view.tvAmount
        val tvPaymentDate: TextView = view.tvPaymentDate
    }

}


