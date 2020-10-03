package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.BankListData
import kotlinx.android.synthetic.main.layout_banks.view.*
import java.util.*

class BankAdapter(
    private var bankList: ArrayList<BankListData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<BankAdapter.BanksViewHolder>() {

    override fun getItemCount(): Int {
        return bankList.size
    }

    private fun getItem(position: Int): BankListData {
        return bankList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BanksViewHolder {
        return BanksViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_banks,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BanksViewHolder, position: Int) {
        val bankItem = getItem(position)
        holder.tvNameBank.text = bankItem.first_name + " " + bankItem.last_name
        holder.tvSSNBank.text = bankItem.ssn_num
        holder.tvPhoneBank.text = bankItem.phone
        holder.tvStreetBank.text = bankItem.street
    }

    class BanksViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvNameBank: TextView = view.tvNameBank
        val tvSSNBank: TextView = view.tvSSNBank
        val tvPhoneBank: TextView = view.tvPhoneBank
        val tvStreetBank: TextView = view.tvStreetBank
    }

}


