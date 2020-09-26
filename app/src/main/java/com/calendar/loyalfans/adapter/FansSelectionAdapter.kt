package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.response.FansData
import com.calendar.loyalfans.utils.Common
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_fans_list.view.imgProfilePic
import kotlinx.android.synthetic.main.layout_fans_list.view.tvProfileName
import kotlinx.android.synthetic.main.layout_fans_list.view.tvUserName
import kotlinx.android.synthetic.main.layout_fans_selection.view.*
import java.util.*
import kotlin.collections.ArrayList

class FansSelectionAdapter(
    private var fansList: ArrayList<FansData>,
    private var fansFilteredList: ArrayList<FansData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<FansSelectionAdapter.FansViewHolder>(), Filterable {


    override fun getItemCount(): Int {
        return fansFilteredList.size
    }

    private fun getItem(position: Int): FansData {
        return fansFilteredList[position]
    }

    fun getFans(): ArrayList<FansData> {
        return fansList
    }

    fun setFans(updatedFans: ArrayList<FansData>) {
        fansFilteredList = updatedFans
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FansViewHolder {
        return FansViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_fans_selection,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FansViewHolder, position: Int) {
        val fansData = getItem(position)
        holder.tvProfileName.text = fansData.display_name
        holder.tvUserName.text = fansData.username
        activity?.let {
            Common.loadImageUsingURL(holder.imgProfilePic,
                fansData.profile_img,
                it)
        }
        holder.cbSelectFan.setOnCheckedChangeListener(null)
        holder.cbSelectFan.isChecked = fansData.isSelected
        holder.cbSelectFan.setOnCheckedChangeListener { buttonView, isChecked ->
            fansData.isSelected = isChecked
            fansFilteredList[position] = fansData
            notifyDataSetChanged()
        }
    }

    class FansViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfilePic: CircleImageView = view.imgProfilePic
        val tvProfileName: TextView = view.tvProfileName
        val tvUserName: TextView = view.tvUserName
        val cbSelectFan: CheckBox = view.cbSelectFan
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                fansFilteredList = if (charSearch.isEmpty()) {
                    fansList
                } else {
                    val resultList = ArrayList<FansData>()
                    for (fansData in fansList) {
                        if (fansData.display_name.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(fansData)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = fansFilteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                fansFilteredList = results?.values as ArrayList<FansData>
                notifyDataSetChanged()
            }

        }
    }

}


