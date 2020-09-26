package com.calendar.loyalfans.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.FavoriteProfileAdapter
import com.calendar.loyalfans.model.request.FansFollowingRequest
import com.calendar.loyalfans.model.response.FavouriteData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_favorite_list.*

class FavoriteProfileFragment(private val favoriteType: String, private val profileId: String) :
    Fragment() {

    companion object {
        fun newInstance(favoriteType: String, profileId: String) =
            FavoriteProfileFragment(favoriteType, profileId)

        fun newInstance(favoriteType: String) =
            FavoriteProfileFragment(favoriteType, Common.getUserId())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getFansByType(favoriteType)
    }

    private fun getFansByType(fansType: String) {
        val fansFollowingRequest =
            FansFollowingRequest(fansType,
                profileId)
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.getFavoriteByType(
            fansFollowingRequest,
            true
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        setUpFansAdapter(it.data)
                    }
                })
    }

    private fun setUpFansAdapter(fansList: ArrayList<FavouriteData>) {
        Common.setupVerticalRecyclerView(rvFavorite, activity)
        rvFavorite.adapter = FavoriteProfileAdapter(fansList, activity)
    }
}