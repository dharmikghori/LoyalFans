package com.calendar.loyalfans.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.ProfilePhotoVideosAdapter
import com.calendar.loyalfans.model.SelectedFileData
import kotlinx.android.synthetic.main.fragment_home.*

class ProfilePhotosFragment : Fragment() {

    companion object {
        fun newInstance() = ProfilePhotosFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_post, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpAdapter()
    }

    private fun setUpAdapter() {
        rvHomePost?.layoutManager =
            GridLayoutManager(activity, 3)
        val postList = ArrayList<SelectedFileData>()
        for (i in 0..9) {
            postList.add(SelectedFileData())
        }
        val photoAdapter = ProfilePhotoVideosAdapter(postList, activity)
        rvHomePost.adapter = photoAdapter
    }

}