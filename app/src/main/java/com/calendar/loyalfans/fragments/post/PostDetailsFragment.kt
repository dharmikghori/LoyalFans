package com.calendar.loyalfans.fragments.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
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


class PostDetailsFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = PostDetailsFragment()
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
    }

    private fun getPostDetails() {
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        val postDetailRequest = PostDetailRequest("99")
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
//        viewProfile.setOnClickListener {
//            activity?.startActivity(Intent(activity, OtherProfileActivity::class.java).putExtra(
//                RequestParams.PROFILE_ID,
//                postData.user_id))
//        }
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
//            onPostAction?.onBookmark(postData, position)
//            postData.bookmark = when (isChecked) {
//                true -> {
//                    "1"
//                }
//                false -> {
//                    "0"
//                }
//            }
//            postListData[position] = postData
//            notifyItemChanged(position)
        }

        cbLikeUnlike.setOnCheckedChangeListener(null)
        cbLikeUnlike.isChecked = when (postData.is_likes) {
            "0" -> false
            "1" -> true
            else ->
                false
        }
        cbLikeUnlike.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            var likesCount = Integer.parseInt(postData.likes)
            var isLikeStatus = postData.is_likes
            when (b) {
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
//            onPostAction?.onLikeUnLike(postData, position)
//            notifyDataSetChanged()
        }
//        if (position == postListData.size - 1) {
//            onPostAction?.onBottomReached(position)
//        }

//        if (isMyPost) {
//            imgMoreOption.visibility = View.VISIBLE
//            btnSendTip.visibility = View.GONE
//            imgMoreOption.setOnClickListener {
//                openOptionMenu(it, postData, position)
//            }
//        } else {
//            imgMoreOption.visibility = View.GONE
//            btnSendTip.visibility = View.VISIBLE
//        }

//        if (!postData.suggestions.isNullOrEmpty()) {
//            if (postData.id == "" && postData.user_id == "") {
//                layPostTopCardView.visibility = View.GONE
//            } else {
//                layPostTopCardView.visibility = View.VISIBLE
//            }
//            laySuggestion.visibility = View.VISIBLE
//            suggestionViewPager.adapter =
//                activity?.let {
//                    val suggestionPagerAdapter =
//                        SuggestionPagerAdapter(it, postData.suggestions)
//                    suggestionPagerAdapter
//
//                }
//            imgNextSuggestion.setOnClickListener {
//                var currentItem = suggestionViewPager.currentItem
//                currentItem += 1
//                suggestionViewPager.setCurrentItem(currentItem,
//                    true)
//            }
//            imgPrevSuggestion.setOnClickListener {
//                var currentItem = suggestionViewPager.currentItem
//                currentItem -= 1
//                suggestionViewPager.setCurrentItem(currentItem,
//                    true)
//            }
//        } else {
        laySuggestion.visibility = View.GONE
//        }
        layComment.setOnClickListener {
//            onPostAction?.onComment(postData, position)
        }

    }

    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == R.id.imgBack) {
                activity?.onBackPressed()
            }
        }
    }


}