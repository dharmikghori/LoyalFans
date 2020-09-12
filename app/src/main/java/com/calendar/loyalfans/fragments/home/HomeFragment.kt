package com.calendar.loyalfans.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.HomePostAdapter
import com.calendar.loyalfans.model.request.PostDetailRequest
import com.calendar.loyalfans.model.request.PostListRequest
import com.calendar.loyalfans.model.response.PostData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.ui.BaseActivity
import com.calendar.loyalfans.ui.MainActivity
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        imgDrawerMenu.setOnClickListener {
            val mainActivity = activity as MainActivity
            if (mainActivity.drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
                mainActivity.drawerLayout?.closeDrawer(GravityCompat.START)
            } else {
                mainActivity.drawerLayout?.openDrawer(GravityCompat.START)
            }
        }
    }

    private var limit = 10
    private var offset = 0

    override fun onResume() {
        super.onResume()
        limit = 10
        offset = 0
        postList = ArrayList()
        homeAdapter = null
        getPosts()

    }


    private fun getPosts() {
        val postListRequest = PostListRequest(offset, limit)
        postListRequest.user_id = Common.getUserId()
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.postList(
            postListRequest, true
        )
            .observe(viewLifecycleOwner, {
                if (it.status && it.data.isNotEmpty()) {
                    if (homeAdapter != null) {
                        refreshAdapter(it.data)
                    } else {
                        setUpAdapter(it.data)
                    }
                }
            })
    }

    private var postList = ArrayList<PostData>()
    private var homeAdapter: HomePostAdapter? = null
    private fun refreshAdapter(postList: ArrayList<PostData>) {
        this.postList.addAll(postList)
        homeAdapter?.notifyDataSetChanged()
    }

    private fun setUpAdapter(postList: ArrayList<PostData>) {
        rvHomePost?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        this.postList.addAll(postList)
        homeAdapter = HomePostAdapter(this.postList, activity)
        rvHomePost.adapter = homeAdapter
        homeAdapter!!.onPostAction = object : HomePostAdapter.OnPostAction {
            override fun onBottomReached(position: Int) {
                offset += limit
                getPosts()
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