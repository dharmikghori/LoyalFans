package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.layout_earning_statement.view.*
import kotlinx.android.synthetic.main.layout_payout_request.view.*
import java.util.*

class StatementsEarningAdapter(
    private var statementList: ArrayList<com.calendar.loyalfans.model.response.StatementData>,
    private val activity: FragmentActivity?,
    private val adapaterType: Int,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val STATEMENT_EARNING = 0
    private val STATEMENT_PAYOUT_REQUEST = 1

    override fun getItemCount(): Int {
        return statementList.size
    }

    private fun getItem(position: Int): com.calendar.loyalfans.model.response.StatementData {
        return statementList[position]
    }

    override fun getItemViewType(position: Int): Int {
        return adapaterType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == STATEMENT_EARNING) {
            return StatementEarningViewHolder(
                LayoutInflater.from(activity).inflate(
                    R.layout.layout_earning_statement,
                    parent,
                    false
                )
            )
        } else if (viewType == STATEMENT_PAYOUT_REQUEST) {
            return StatementPayoutRequestViewHolder(
                LayoutInflater.from(activity).inflate(
                    R.layout.layout_payout_request,
                    parent,
                    false
                )
            )
        } else {
            return StatementPayoutRequestViewHolder(
                LayoutInflater.from(activity).inflate(
                    R.layout.layout_payout_request,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val statementItem = getItem(position)
        if (holder is StatementEarningViewHolder) {
            holder.tvStatementDate.text = Common.formatDate(statementItem.payment_date)
            holder.tvStatementType.text = statementItem.type
            holder.tvStatementAmount.text = statementItem.amount
        } else if (holder is StatementPayoutRequestViewHolder) {
            holder.tvPayoutDate.text = Common.formatDate(statementItem.created)
            holder.tvInvoiceType.text = statementItem.id
            holder.tvInvoicePrice.text = activity?.getString(R.string.dollar) + statementItem.amount
            holder.tvInvoiceStatus.text = when (statementItem.status) {
                "0" -> {
                    "Pending"
                }
                "1" -> {
                    "Success"
                }
                else -> "Cancelled"
            }
            if (activity != null) {
                holder.cardViewPayoutMain.setCardBackgroundColor(when (statementItem.status) {
                    "0" -> {
                        activity.resources.getColor(R.color.pending)
                    }
                    "1" -> {
                        activity.resources.getColor(R.color.success)
                    }
                    else -> activity.resources.getColor(R.color.canceled)

                })
            }
        }

    }

    class StatementEarningViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStatementDate: TextView = view.tvStatementDate
        val tvStatementType: TextView = view.tvStatementType
        val tvStatementAmount: TextView = view.tvStatementAmount
    }

    class StatementPayoutRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardViewPayoutMain: CardView = view.cardViewPayoutMain
        val tvPayoutDate: TextView = view.tvPayoutDate
        val tvInvoiceType: TextView = view.tvInvoiceType
        val tvInvoicePrice: TextView = view.tvInvoicePrice
        val tvInvoiceStatus: TextView = view.tvInvoiceStatus
    }

}


