package com.calendar.loyalfans.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.StateCityData

class CustomDropDownAdapter(val context: Context, var dataSource: ArrayList<StateCityData>) :
    BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.custom_spinner_item, parent, false)
            vh = ItemHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.tvStateCityName?.text = dataSource[position].name

        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position]
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private class ItemHolder(row: View?) {
        val tvStateCityName: TextView? = row?.findViewById(R.id.tvStateCityName)

    }
}