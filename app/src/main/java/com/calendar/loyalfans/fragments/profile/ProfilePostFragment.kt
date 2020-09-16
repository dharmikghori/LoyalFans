package com.calendar.loyalfans.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.MyProfilePostAdapter
import com.calendar.loyalfans.fragments.post.EditPostFragment
import com.calendar.loyalfans.model.request.PostDetailRequest
import com.calendar.loyalfans.model.request.PostListRequest
import com.calendar.loyalfans.model.response.PostData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.ui.BaseActivity
import com.calendar.loyalfans.ui.MainActivity
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_profile_post.*

class ProfilePostFragment : Fragment() {

    private var limit = 10
    private var offset = 0

    companion object {
        fun newInstance() = ProfilePostFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_post, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getPosts()
    }

    private fun getPosts() {
        val postListRequest = PostListRequest(offset, limit, 0, Common.getUserId())
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.profilePost(
            postListRequest, true
        )
            .observe(viewLifecycleOwner, {
                if (it.status && it.data.isNotEmpty()) {
                    if (myProfilePostAdapter != null) {
                        refreshAdapter(it.data)
                    } else {
                        setUpAdapter(it.data)
                    }
                }
            })
    }

    private var postList = ArrayList<PostData>()
    private var myProfilePostAdapter: MyProfilePostAdapter? = null
    private fun refreshAdapter(postList: ArrayList<PostData>) {
        this.postList.addAll(postList)
        myProfilePostAdapter?.notifyDataSetChanged()
    }

    private fun setUpAdapter(lastPostList: ArrayList<PostData>) {
        rvHomePost?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        this.postList.addAll(lastPostList)
        myProfilePostAdapter = MyProfilePostAdapter(this.postList, activity, true)
        rvHomePost.adapter = myProfilePostAdapter
        myProfilePostAdapter!!.onPostAction = object : MyProfilePostAdapter.OnPostAction {
            override fun onBottomReached(position: Int) {
                if (postList.size % limit == 0) {
                    offset += limit
                    getPosts()
                }
            }

            override fun onLikeUnLike(postData: PostData, position: Int) {
                onLikeUnLikeAPI(postData, position)
            }

            override fun onComment(postData: PostData, position: Int) {
                TODO("Not yet implemented")
            }

            override fun onBookmark(postData: PostData, position: Int) {
                TODO("Not yet implemented")
            }

            override fun onEditPost(postData: PostData, position: Int) {
                (activity as MainActivity).loadFragmentWithEditPostListener(14,
                    postData,
                    object : EditPostFragment.OnPostEdit {
                        override fun onEdit(postData: PostData) {
                            (activity as MainActivity).onBackPressed()
                            lastPostList[position] = postData
                            myProfilePostAdapter!!.notifyItemChanged(position)
                        }
                    })
            }

            override fun onDeletePost(postData: PostData, position: Int) {
                val postDetailRequest = PostDetailRequest(postData.id)
                postDetailRequest.user_id = Common.getUserId()
                val baseViewModel =
                    activity?.let { ViewModelProvider(it).get(BaseViewModel::class.java) }
                activity?.let {
                    baseViewModel?.deletePost(
                        postDetailRequest, true
                    )?.observe(it, { it1 ->
                        if (it1.status) {
                            Common.showToast(activity!!, it1.msg)
                            lastPostList.removeAt(position)
                            myProfilePostAdapter!!.notifyItemRemoved(position)
                        }
                    })
                }
            }
        }
    }

    private fun onLikeUnLikeAPI(postData: PostData, position: Int) {
        val postDetailRequest = PostDetailRequest(postData.id)
        postDetailRequest.user_id = Common.getUserId()
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.likeUnlikePost(
            postDetailRequest, false
        )
            .observe(viewLifecycleOwner, {
                if (it.status) {
                    Common.showToast(BaseActivity.getActivity(), it.msg)
                }
            })
    }
}