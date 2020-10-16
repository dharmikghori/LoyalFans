package com.calendar.loyalfans.fragments.post

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity.Companion.checkReplyOrNormalComment
import com.calendar.loyalfans.adapter.CommentAdapter
import com.calendar.loyalfans.model.request.AddCommentRequest
import com.calendar.loyalfans.model.request.CommentRequest
import com.calendar.loyalfans.model.request.PostDetailRequest
import com.calendar.loyalfans.model.response.CommentData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_comment.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*

class CommentsFragment(private val postId: String) : Fragment(), View.OnClickListener {
    private var replyCommentID = ""

    companion object {
        fun newInstance(
            postId: String,
        ): Fragment {

            return CommentsFragment(postId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvPostComment.setOnClickListener(this)
        imgBack.setOnClickListener(this)
        getComments()
        checkReplyOrNormalComment.value = ""
        tvToolBarName.text = getString(R.string.comments_header)
//        if (activity is MainActivity) {
//            (activity as MainActivity).manageBottomNavigationVisibility(false)}
        checkReplyOrNormalComment.removeObserver {}
        checkReplyOrNormalComment.observe(viewLifecycleOwner, {
            replyCommentID = ""
            if (it == "") {
                etComment.hint = getString(R.string.write_comment)
            } else {
                val replyCommentData = it.split(",")
                replyCommentID = replyCommentData[0]
                etComment.hint = "Reply to ${replyCommentData[1]}"
            }
        })

    }

    private fun getComments() {
        val postDetailRequest =
            PostDetailRequest(postId)
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.getCommentsByPostId(
            postDetailRequest, true
        )
            .observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        setUpCommentsAdapter(it.data)
                    }
                })
    }

    private fun setUpCommentsAdapter(comments: ArrayList<CommentData>) {
        Common.setupVerticalRecyclerView(rvComments, activity)
        val commentAdapter = CommentAdapter(comments, activity, false, postId)
        rvComments.adapter = commentAdapter
        rvComments.smoothScrollToPosition(comments.size)
        commentAdapter.onCommentLikeUnLike = object : CommentAdapter.OnCommentLikeUnLike {
            override fun onLikeUnLikeComment(commentRequest: CommentRequest) {
                makeLikeUnLikeComment(commentRequest)
            }
        }
    }

    private fun makeLikeUnLikeComment(commentRequest: CommentRequest) {
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.likeUnLikeComment(
            commentRequest, true
        ).observe(viewLifecycleOwner,
            {
                if (it.status) {
                    activity?.let { it1 -> Common.showToast(it1, it.msg) }
                }
            })
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == R.id.tvPostComment) {
                onPostComment()
            } else if (v.id == R.id.imgBack) {
                activity?.onBackPressed()
            }
        }
    }

    private fun onPostComment() {
        if (checkValidation()) {
            val addCommentRequest =
                AddCommentRequest(postId, etComment.text.toString(), when (replyCommentID.length) {
                    0 -> {
                        ""
                    }
                    else -> {
                        replyCommentID
                    }
                })
            val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
            baseViewModel.addComments(
                addCommentRequest, true
            )
                .observe(viewLifecycleOwner,
                    {
                        if (it.status) {
                            activity?.let { it1 ->
                                Common.showToast(it1, it.msg)
                                val imm: InputMethodManager =
                                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(etComment.windowToken, 0)
                                requireActivity().onBackPressed()
                            }
                        }
                    })
        }
    }

    private fun checkValidation(): Boolean {

        if (etComment.text.toString().isEmpty()) {
            activity?.let { Common.showToast(it, getString(R.string.add_comment_validation)) }
            return false
        }
        return true
    }

}