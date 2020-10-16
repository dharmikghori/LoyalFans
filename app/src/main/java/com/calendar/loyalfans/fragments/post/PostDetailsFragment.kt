package com.calendar.loyalfans.fragments.post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.activities.MainActivity
import com.calendar.loyalfans.adapter.PostImageVideoPagerAdapter
import com.calendar.loyalfans.model.request.PostDetailRequest
import com.calendar.loyalfans.model.response.PostData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_post_details.*
import kotlinx.android.synthetic.main.layout_like_comment_bottom_view.*
import kotlinx.android.synthetic.main.layout_post_message_and_photos.*
import kotlinx.android.synthetic.main.layout_post_top_view.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*


class PostDetailsFragment(val post_id: String, val type: String) : Fragment(),
    View.OnClickListener {

    companion object {
        fun newInstance(post_id: String, type: String) = PostDetailsFragment(post_id, type)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_post_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.post)
        imgBack.setOnClickListener(this)
        getPostDetails()
        Log.d("NotificationTypeComment", type)
        layComment.setOnClickListener {
            (activity as MainActivity).loadFragment(17, post_id)
        }
        if (type == Common.Companion.Notifications.ADD_COMMENT.notificationTypeValue() ||
            type == Common.Companion.Notifications.LIKE_COMMENT.notificationTypeValue()
        ) {
            layComment.performClick()
        }
    }

    private fun getPostDetails() {
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        val postDetailRequest = PostDetailRequest(post_id)
        baseViewModel.getPostDetails(postDetailRequest, true
        ).observe(viewLifecycleOwner, {
            if (it.status) {
                setUpPostData(it.data)
            }
        })
    }

    private fun setUpPostData(postData: PostData) {
        btnSendTip.setOnClickListener {
            activity?.let {
                Common.showSendDialog(it,
                    postData)
            }
        }
        tvActivityMessage.text = postData.content
        tvProfileName.text = postData.display_name
        tvUserName.text = "@" + postData.username
        tvTotalComment.text = postData.comments + " Comments"
        tvTotalLike.text = postData.likes + " Likes"
        activity?.let {
            photos_viewpager.adapter =
                PostImageVideoPagerAdapter(it, postData.images)
            tabLayout.visibility = View.GONE
        }

        activity?.let {
            Common.loadImageUsingURL(imgProfilePic,
                postData.profile_img, it, true)
        }
        cbBookmark.setOnCheckedChangeListener(null)
        cbBookmark.isChecked = when (postData.bookmark) {
            "0" -> false
            "1" -> true
            else ->
                false
        }
        cbBookmark.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            onFavouriteUnFavoritePost(postData)
        }

        cbLikeUnlike.setOnCheckedChangeListener(null)
        cbLikeUnlike.isChecked = when (postData.is_likes) {
            "0" -> false
            "1" -> true
            else ->
                false
        }
        cbLikeUnlike.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            onLikeUnLikeAPI(postData, b)
        }
        laySuggestion.visibility = View.GONE


    }

    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == R.id.imgBack) {
                activity?.onBackPressed()
            }
        }
    }


    private fun onFavouriteUnFavoritePost(postData: PostData) {
        val postDetailRequest = PostDetailRequest(postData.id)
        postDetailRequest.user_id = Common.getUserId()
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.favUnFavPost(
            postDetailRequest, false
        )
            .observe(viewLifecycleOwner, {
                if (it.status) {
                    Common.showToast(BaseActivity.getActivity(), it.msg)
                }
            })
    }

    private fun onLikeUnLikeAPI(postData: PostData, isLike: Boolean) {
        val postDetailRequest = PostDetailRequest(postData.id)
        postDetailRequest.user_id = Common.getUserId()
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.likeUnlikePost(
            postDetailRequest, false
        )
            .observe(viewLifecycleOwner, {
                if (it.status) {
                    Common.showToast(BaseActivity.getActivity(), it.msg)
                    var likesCount = Integer.parseInt(postData.likes)
                    var isLikeStatus = postData.is_likes
                    when (isLike) {
                        true -> {
                            likesCount += 1
                            isLikeStatus = "1"
                        }
                        false -> {
                            likesCount -= 1
                            isLikeStatus = "0"
                        }
                    }
                    postData.likes = likesCount.toString()
                    postData.is_likes = isLikeStatus
                    tvTotalLike.text = likesCount.toString()

                }
            })
    }


}