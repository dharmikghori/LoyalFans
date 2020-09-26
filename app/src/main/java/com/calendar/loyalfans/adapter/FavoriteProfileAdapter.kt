package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.utils.Common
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_fans_list.view.*
import java.util.*

class FavoriteProfileAdapter(
    private var fansList: ArrayList<com.calendar.loyalfans.model.response.FavouriteData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<FavoriteProfileAdapter.FavoriteProfileVideHolder>() {


    override fun getItemCount(): Int {
        return fansList.size
    }

    private fun getItem(position: Int): com.calendar.loyalfans.model.response.FavouriteData {
        return fansList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProfileVideHolder {
        return FavoriteProfileVideHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_favorite_profile,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteProfileVideHolder, position: Int) {
        val favouriteData = getItem(position)
        holder.tvProfileName.text = favouriteData.display_name
        holder.tvUserName.text = favouriteData.username
        activity?.let { Common.loadImageUsingURL(holder.imgProfilePic, favouriteData.profile_img, it) }
    }

    class FavoriteProfileVideHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfilePic: CircleImageView = view.imgProfilePic
        val tvProfileName: TextView = view.tvProfileName
        val tvUserName: TextView = view.tvUserName
    }

}


