package com.calendar.loyalfans.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.OtherProfileActivity
import com.calendar.loyalfans.model.response.SuggestionData
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.RequestParams
import org.apache.commons.lang3.StringUtils


class SuggestionPagerAdapter(
    private val context: Context,
    suggestionData: ArrayList<SuggestionData>,
) :
    PagerAdapter() {
    private val suggestionList = suggestionData
    override fun getCount(): Int {
        return suggestionList.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        val viewPager = container as ViewPager
        val view = obj as View
        viewPager.removeView(view)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View =
            inflater.inflate(R.layout.fragment_suggestion_pager, container, false)
        val suggestionData = suggestionList[position]
        val imgProfileCover = view.findViewById<ImageView>(R.id.imgProfileCover)
        val imgProfilePic = view.findViewById<ImageView>(R.id.imgProfilePic)
        val tvProfileName = view.findViewById<TextView>(R.id.tvProfileName)
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        val layMainSuggestion = view.findViewById<RelativeLayout>(R.id.layMainSuggestion)
        val subscriptionAmount = view.findViewById<TextView>(R.id.subscriptionAmount)
        layMainSuggestion.setOnClickListener {
            context.startActivity(Intent(context, OtherProfileActivity::class.java).putExtra(
                RequestParams.PROFILE_ID,
                suggestionData.id))
        }
        Common.loadImageUsingURL(imgProfilePic,
            suggestionData.profile_img,
            context, true)
        Common.loadImageUsingURL(imgProfileCover,
            suggestionData.cover_img,
            context, true)
        subscriptionAmount.text = when (suggestionData.business_type) {
            "FREE" -> {
                suggestionData.business_type
            }
            else -> {
                val subscriptionPlans = suggestionData.subscription_plans
                if (subscriptionPlans != null) {
                    var subscriptionDetailStr = ""
                    for (subscriptionData in subscriptionPlans) {
                        val months = subscriptionData.months
                        val amount = subscriptionData.amount
                        subscriptionDetailStr = subscriptionDetailStr +
                                context.getString(R.string.dollar) + " " + amount + " / " + months + " MONTH | "
                    }
                    subscriptionDetailStr =
                        StringUtils.removeEnd(subscriptionDetailStr, " | ")
                    subscriptionDetailStr
                } else {
                    ""
                }
            }
        }
        subscriptionAmount.isSelected = true
        tvProfileName.text = suggestionData.display_name
        tvUserName.text = suggestionData.username
        val viewPager = container as ViewPager
        viewPager.addView(view, 0)
        return view
    }
}