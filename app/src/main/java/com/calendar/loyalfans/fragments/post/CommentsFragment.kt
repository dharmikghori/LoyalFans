package com.calendar.loyalfans.fragments.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.CommentAdapter
import com.calendar.loyalfans.model.request.AddCommentRequest
import com.calendar.loyalfans.model.request.PostDetailRequest
import com.calendar.loyalfans.model.response.CommentData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_comment.*

class CommentsFragment(val postId: String) : Fragment(), View.OnClickListener {

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
        getComments()
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
        rvComments.adapter = CommentAdapter(comments, activity)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == R.id.tvPostComment) {
                onPostComment()
            }
        }
    }

    private fun onPostComment() {
        if (checkValidation()) {
            val addCommentRequest =
                AddCommentRequest(postId, etComment.text.toString())
            val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
            baseViewModel.addComments(
                addCommentRequest, true
            )
                .observe(viewLifecycleOwner,
                    {
                        if (it.status) {
                            activity?.let { it1 ->
                                Common.showToast(it1, it.msg)
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