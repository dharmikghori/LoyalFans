package com.calendar.loyalfans.fragments.post


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.request.PostEditRequest
import com.calendar.loyalfans.model.response.PostData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_edit_post.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*

class EditPostFragment(private var postData: PostData, private var onPostEdit: OnPostEdit) :
    Fragment(),
    View.OnClickListener {

    companion object {
        fun newInstance(
            postData: PostData,
            onPostEdit: OnPostEdit,
        ): EditPostFragment {
            return EditPostFragment(postData, onPostEdit)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_post, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.edit_post)
        btnAddPost.setOnClickListener(this)
        imgBack.setOnClickListener(this)
        etPostMessage.setText(postData.content)
    }


    interface OnPostEdit {
        fun onEdit(postData: PostData)
    }


    private fun checkValidation(): Boolean {
        if (etPostMessage.text.toString().isEmpty()) {
            activity?.let { Common.showToast(it, getString(R.string.content_validation)) }
            return false
        }
        return true
    }

    override fun onClick(view: View?) {
        if (view != null) {
            if (view.id == R.id.imgBack) {
                activity?.onBackPressed()
            } else if (view.id == R.id.btnAddPost) {

                if (checkValidation()) {
                    val postEditRequest =
                        PostEditRequest(postData.id, etPostMessage.text.toString())
                    postEditRequest.user_id = Common.getUserId()
                    val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
                    baseViewModel.updatePost(
                        postEditRequest, true
                    )
                        .observe(this, {
                            if (it.status) {
                                postData.content = etPostMessage.text.toString()
                                onPostEdit.onEdit(postData)
                            }
                        })
                }
            }
        }
    }
}


