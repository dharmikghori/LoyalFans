package com.calendar.loyalfans.retrofit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.calendar.loyalfans.BuildConfig
import com.calendar.loyalfans.model.request.*
import com.calendar.loyalfans.model.response.*
import com.calendar.loyalfans.utils.Common
import com.google.gson.Gson


class BaseViewModel : ViewModel() {

    fun login(
        loginRequestData: LoginRequest,
        progressShow: Boolean,
    ): MutableLiveData<LoginResponse> {
        val apiName = APIServices.SIGN_IN
        val loginResponseData: MutableLiveData<LoginResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(loginRequestData, apiName)
        apiServices?.login(
            loginRequestData.email,
            loginRequestData.password,
            loginRequestData.device_id,
            loginRequestData.device_type,
            loginRequestData.fcm_token,
            loginRequestData.type,
            loginRequestData.google_id,
            loginRequestData.fb_id,
            loginRequestData.name
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val loginResponse = response as LoginResponse
                printStrJson(loginResponse, apiName)
                loginResponseData.value = loginResponse
            }
        }))
        return loginResponseData
    }


    fun registerNewUser(
        registrationRequest: RegistrationRequest,
        progressShow: Boolean,
    ): MutableLiveData<LoginResponse> {
        val apiName = APIServices.SIGN_UP
        val loginResponseData: MutableLiveData<LoginResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(registrationRequest, apiName)
        apiServices?.registration(
            registrationRequest.email,
            registrationRequest.name,
            registrationRequest.password,
            registrationRequest.device_id,
            registrationRequest.device_type,
            registrationRequest.fcm_token,
            registrationRequest.type,
            registrationRequest.google_id,
            registrationRequest.fb_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val loginResponse = response as LoginResponse
                printStrJson(loginResponse, apiName)
                loginResponseData.value = loginResponse
            }
        }))
        return loginResponseData
    }

    fun forgotPassword(
        forgotPasswordRequest: ForgotPasswordRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.FORGOT_PASSWORD
        val forgotPasswordData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(forgotPasswordRequest, apiName)
        apiServices?.forgotPassword(
            forgotPasswordRequest.email
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val forgotPasswordResponse = response as BaseResponse
                printStrJson(forgotPasswordResponse, apiName)
                forgotPasswordData.value = forgotPasswordResponse
            }
        }))
        return forgotPasswordData
    }

    fun updateFcmToken(
        fcmTokenRequest: UpdateFCMToken,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.UPDATE_FCM_TOKEN
        val fcmTokenData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(fcmTokenRequest, apiName)
        apiServices?.updateFcmToken(
            fcmTokenRequest.user_id,
            fcmTokenRequest.fcm_token
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val updateFCMTokenResponse = response as BaseResponse
                printStrJson(updateFCMTokenResponse, apiName)
                fcmTokenData.value = updateFCMTokenResponse
            }
        }))
        return fcmTokenData
    }


    fun changePassword(
        changePasswordRequest: ChangePasswordRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.CHANGE_PASSWORD
        val changePasswordData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(changePasswordRequest, apiName)
        apiServices?.changePassword(
            changePasswordRequest.user_id,
            changePasswordRequest.current_password,
            changePasswordRequest.new_password,
            changePasswordRequest.confirm_password
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val changePasswordResponse = response as BaseResponse
                printStrJson(changePasswordResponse, apiName)
                changePasswordData.value = changePasswordResponse
            }
        }))
        return changePasswordData
    }


    fun likeUnlikePost(
        likeFavouriteRequest: PostDetailRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.LIKE_POST
        val likeUnlikeData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(likeFavouriteRequest, apiName)
        apiServices?.likePost(
            likeFavouriteRequest.user_id,
            likeFavouriteRequest.post_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val likeUnlikeResponse = response as BaseResponse
                printStrJson(likeUnlikeResponse, apiName)
                likeUnlikeData.value = likeUnlikeResponse
            }
        }))
        return likeUnlikeData
    }

    fun favUnFavPost(
        likeFavouriteRequest: PostDetailRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.FAVORITE_POST
        val favUnFavData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(likeFavouriteRequest, apiName)
        apiServices?.favoritePost(
            likeFavouriteRequest.user_id, likeFavouriteRequest.post_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val favUnFavPostResponse = response as BaseResponse
                printStrJson(favUnFavPostResponse, apiName)
                favUnFavData.value = favUnFavPostResponse
            }
        }))
        return favUnFavData
    }

    fun searchUser(
        searchUserRequest: SearchUserRequest,
        progressShow: Boolean,
    ): MutableLiveData<SearchUserData> {
        val apiName = APIServices.SEARCH_USER
        val searchUserData: MutableLiveData<SearchUserData> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(searchUserRequest, apiName)
        apiServices?.searchUser(
            searchUserRequest.user_id,
            searchUserRequest.text
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val searchUserResponse = response as SearchUserData
                printStrJson(searchUserResponse, apiName)
                searchUserData.value = searchUserResponse
            }
        }))
        return searchUserData
    }

    fun postList(
        postListRequest: PostListRequest,
        progressShow: Boolean,
    ): MutableLiveData<PostResponse> {
        val apiName = APIServices.POST_LIST
        val postData: MutableLiveData<PostResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(postListRequest, apiName)
        apiServices?.postList(
            postListRequest.user_id,
            postListRequest.offset.toString(),
            postListRequest.limit.toString()
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val postResponse = response as PostResponse
                printStrJson(postResponse, apiName)
                postData.value = postResponse
            }
        }))
        return postData
    }


    fun profilePost(
        postListRequest: PostListRequest,
        progressShow: Boolean,
    ): MutableLiveData<PostResponse> {
        postListRequest.user_id = Common.getUserId()
        val apiName = APIServices.POST_LIST
        val postData: MutableLiveData<PostResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(postListRequest, apiName)
        apiServices?.profilePost(
            postListRequest.user_id,
            postListRequest.offset.toString(),
            postListRequest.limit.toString(),
            postListRequest.profile_id,
            postListRequest.type.toString()
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val postResponse = response as PostResponse
                printStrJson(postResponse, apiName)
                postData.value = postResponse
            }
        }))
        return postData
    }

    fun updatePost(
        postEditRequest: PostEditRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.UPDATE_POST
        val updatePostData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(postEditRequest, apiName)
        apiServices?.updatePost(
            postEditRequest.user_id,
            postEditRequest.post_id,
            postEditRequest.content
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val updatePostResponse = response as BaseResponse
                printStrJson(updatePostResponse, apiName)
                updatePostData.value = updatePostResponse
            }
        }))
        return updatePostData
    }

    fun deletePost(
        postEditRequest: PostDetailRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.DELETE_POST
        val deletePostData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(postEditRequest, apiName)
        apiServices?.deletePost(
            postEditRequest.user_id,
            postEditRequest.post_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val deletePostResponse = response as BaseResponse
                printStrJson(deletePostResponse, apiName)
                deletePostData.value = deletePostResponse
            }
        }))
        return deletePostData
    }

    fun getProfile(
        profileDetailRequest: ProfileDetailRequest,
        progressShow: Boolean,
    ): MutableLiveData<ProfileResponse> {
        val apiName = APIServices.GET_PROFILE
        val profileData: MutableLiveData<ProfileResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(profileDetailRequest, apiName)
        apiServices?.getProfile(
            profileDetailRequest.user_id,
            profileDetailRequest.profile_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val profileResponse = response as ProfileResponse
                printStrJson(profileResponse, apiName)
                profileData.value = profileResponse
            }
        }))
        return profileData
    }

    fun getEditProfile(
        profileDetailRequest: ProfileDetailRequest,
        progressShow: Boolean,
    ): MutableLiveData<ProfileResponse> {
        val apiName = APIServices.GET_EDIT_PROFILE
        val profileData: MutableLiveData<ProfileResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(profileDetailRequest, apiName)
        apiServices?.getEditProfile(
            profileDetailRequest.user_id,
            profileDetailRequest.profile_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val profileResponse = response as ProfileResponse
                printStrJson(profileResponse, apiName)
                profileData.value = profileResponse
            }
        }))
        return profileData
    }

    fun updateProfile(
        updateProfileRequest: UpdateProfileRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.UPDATE_PROFILE
        val updateProfileData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        printStrRequestJson(updateProfileRequest, apiName)
        apiServices?.updateProfile(
            updateProfileRequest.user_id,
            updateProfileRequest.display_name,
            updateProfileRequest.location,
            updateProfileRequest.about,
            updateProfileRequest.website,
            updateProfileRequest.profile_img,
            updateProfileRequest.banner_img,
            updateProfileRequest.username
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val updateProfileResponse = response as BaseResponse
                printStrJson(updateProfileResponse, apiName)
                updateProfileData.value = updateProfileResponse
            }
        }))
        return updateProfileData
    }

    fun getSubscriptionPlan(
        progressShow: Boolean,
    ): MutableLiveData<SubscriptionResponse> {
        val apiName = APIServices.SUBSCRIPTION_PLAN
        val subscriptionData: MutableLiveData<SubscriptionResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        val baseRequest = BaseRequest(Common.getUserId())
        printStrRequestJson(baseRequest, apiName)
        apiServices?.getSubscriptionPlan(
            baseRequest.user_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val subscriptionResponse = response as SubscriptionResponse
                printStrJson(subscriptionResponse, apiName)
                subscriptionData.value = subscriptionResponse
            }
        }))
        return subscriptionData
    }

    fun setSubscriptionPlan(
        subscriptionRequestData: String,
        progressShow: Boolean,
    ): MutableLiveData<SubscriptionResponse> {
        val apiName = APIServices.SUBSCRIPTION_PLAN
        val subscriptionData: MutableLiveData<SubscriptionResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        val baseRequest = BaseRequest(Common.getUserId())
        printStrRequestJson(subscriptionRequestData, apiName)
        val toJson = Gson().toJson(subscriptionRequestData)
        apiServices?.setSubscriptionPlan(
            baseRequest.user_id, toJson
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val subscriptionResponse = response as SubscriptionResponse
                printStrJson(subscriptionResponse, apiName)
                subscriptionData.value = subscriptionResponse
            }
        }))
        return subscriptionData
    }

    fun getFansByType(
        fansFollowingRequest: FansFollowingRequest,
        progressShow: Boolean,
    ): MutableLiveData<FansResponse> {
        val apiName = APIServices.GET_FANS
        val fansData: MutableLiveData<FansResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        fansFollowingRequest.user_id = Common.getUserId()
        printStrRequestJson(fansFollowingRequest, apiName)
        apiServices?.getFansByType(
            fansFollowingRequest.user_id, fansFollowingRequest.profile_id, fansFollowingRequest.type
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val fansResponse = response as FansResponse
                printStrJson(fansResponse, apiName)
                fansData.value = fansResponse
            }
        }))
        return fansData
    }

    fun getFollowingByType(
        fansFollowingRequest: FansFollowingRequest,
        progressShow: Boolean,
    ): MutableLiveData<FansResponse> {
        val apiName = APIServices.GET_FOLLOWING
        val fansData: MutableLiveData<FansResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        fansFollowingRequest.user_id = Common.getUserId()
        printStrRequestJson(fansFollowingRequest, apiName)
        apiServices?.getFollowingByType(
            fansFollowingRequest.user_id, fansFollowingRequest.profile_id, fansFollowingRequest.type
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val fansResponse = response as FansResponse
                printStrJson(fansResponse, apiName)
                fansData.value = fansResponse
            }
        }))
        return fansData
    }

    fun getFavoriteByType(
        fansFollowingRequest: FansFollowingRequest,
        progressShow: Boolean,
    ): MutableLiveData<FavouriteResponse> {
        val apiName = APIServices.GET_FAVORITE
        val favouriteData: MutableLiveData<FavouriteResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        fansFollowingRequest.user_id = Common.getUserId()
        printStrRequestJson(fansFollowingRequest, apiName)
        apiServices?.getFavorite(
            fansFollowingRequest.user_id, fansFollowingRequest.type
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val favouriteResponse = response as FavouriteResponse
                printStrJson(favouriteResponse, apiName)
                favouriteData.value = favouriteResponse
            }
        }))
        return favouriteData
    }


    fun getPPVHistory(
        profileDetailRequest: ProfileDetailRequest,
        progressShow: Boolean,
    ): MutableLiveData<PpvHistoryResponse> {
        val apiName = APIServices.PPV_HISTORY
        val historyData: MutableLiveData<PpvHistoryResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        profileDetailRequest.user_id = Common.getUserId()
        printStrRequestJson(profileDetailRequest, apiName)
        apiServices?.ppvHistory(
            profileDetailRequest.user_id, profileDetailRequest.profile_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val ppvHistoryResponse = response as PpvHistoryResponse
                printStrJson(ppvHistoryResponse, apiName)
                historyData.value = ppvHistoryResponse
            }
        }))
        return historyData
    }


    fun getCommentsByPostId(
        postDetailRequest: PostDetailRequest,
        progressShow: Boolean,
    ): MutableLiveData<CommentResponse> {
        val apiName = APIServices.GET_COMMENTS
        val commentData: MutableLiveData<CommentResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        postDetailRequest.user_id = Common.getUserId()
        printStrRequestJson(postDetailRequest, apiName)
        apiServices?.getCommentsByPostId(
            postDetailRequest.user_id, postDetailRequest.post_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val commentResponse = response as CommentResponse
                printStrJson(commentResponse, apiName)
                commentData.value = commentResponse
            }
        }))
        return commentData
    }

    fun addComments(
        addCommentRequest: AddCommentRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.ADD_COMMENTS
        val addCommentData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        addCommentRequest.user_id = Common.getUserId()
        printStrRequestJson(addCommentRequest, apiName)
        if (addCommentRequest.comment_id == "") {
            apiServices?.addComments(
                addCommentRequest.user_id, addCommentRequest.post_id, addCommentRequest.comment
            )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
                override fun onResponse(response: Any) {
                    val baseResponse = response as BaseResponse
                    printStrJson(baseResponse, apiName)
                    addCommentData.value = baseResponse
                }
            }))
        } else {
            apiServices?.replyComment(
                addCommentRequest.user_id,
                addCommentRequest.post_id,
                addCommentRequest.comment,
                addCommentRequest.comment_id
            )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
                override fun onResponse(response: Any) {
                    val baseResponse = response as BaseResponse
                    printStrJson(baseResponse, apiName)
                    addCommentData.value = baseResponse
                }
            }))
        }
        return addCommentData
    }

    fun likeUnLikeComment(
        commentRequest: CommentRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.LIKE_COMMENT
        val likeCommentData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        commentRequest.user_id = Common.getUserId()
        printStrRequestJson(commentRequest, apiName)
        apiServices?.likeComment(
            commentRequest.user_id,
            commentRequest.post_id,
            commentRequest.comment_id,
            commentRequest.type
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val baseResponse = response as BaseResponse
                printStrJson(baseResponse, apiName)
                likeCommentData.value = baseResponse
            }
        }))
        return likeCommentData
    }

    fun sendTip(
        sendTipRequest: SendTipRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.SEND_TIP
        val sendTipData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        sendTipRequest.user_id = Common.getUserId()
        printStrRequestJson(sendTipRequest, apiName)
        apiServices?.sendTip(
            sendTipRequest.user_id,
            sendTipRequest.owner_id,
            sendTipRequest.amount,
            sendTipRequest.post_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val baseResponse = response as BaseResponse
                printStrJson(baseResponse, apiName)
                sendTipData.value = baseResponse
            }
        }))
        return sendTipData
    }

    fun followUser(
        profileDetailRequest: ProfileDetailRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.FOLLOW_USER
        val followUserData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        profileDetailRequest.user_id = Common.getUserId()
        printStrRequestJson(profileDetailRequest, apiName)
        apiServices?.followUser(
            profileDetailRequest.user_id, profileDetailRequest.profile_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val followResponse = response as BaseResponse
                printStrJson(followResponse, apiName)
                followUserData.value = followResponse
            }
        }))
        return followUserData
    }

    fun addCard(
        addCardRequest: AddCardRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.ADD_CARD
        val addCardData: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        addCardRequest.user_id = Common.getUserId()
        printStrRequestJson(addCardRequest, apiName)
        apiServices?.addCard(
            addCardRequest.user_id,
            addCardRequest.street,
            addCardRequest.city,
            addCardRequest.state,
            addCardRequest.zip,
            addCardRequest.country,
            addCardRequest.name,
            addCardRequest.card_num,
            addCardRequest.exp_year,
            addCardRequest.exp_month,
            addCardRequest.cvv,
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val addCardResponse = response as BaseResponse
                printStrJson(addCardResponse, apiName)
                addCardData.value = addCardResponse
            }
        }))
        return addCardData
    }

    fun followPaidUser(
        paidSubscriptionRequest: PaidSubscriptionRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.PAID_SUBSCRIPTION
        val paidSubscription: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        paidSubscriptionRequest.user_id = Common.getUserId()
        printStrRequestJson(paidSubscriptionRequest, apiName)
        apiServices?.followPaidUser(
            paidSubscriptionRequest.user_id,
            paidSubscriptionRequest.plan_id,
            paidSubscriptionRequest.month,
            paidSubscriptionRequest.price,
            paidSubscriptionRequest.owner_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val paidSubscriptionResponse = response as BaseResponse
                printStrJson(paidSubscriptionResponse, apiName)
                paidSubscription.value = paidSubscriptionResponse
            }
        }))
        return paidSubscription
    }

    fun unFollowPaidUser(
        paidSubscriptionRequest: PaidSubscriptionRequest,
        progressShow: Boolean,
    ): MutableLiveData<BaseResponse> {
        val apiName = APIServices.CANCEL_PAID_SUBSCRIPTION
        val paidSubscription: MutableLiveData<BaseResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        paidSubscriptionRequest.user_id = Common.getUserId()
        printStrRequestJson(paidSubscriptionRequest, apiName)
        apiServices?.unFollowPaidUser(
            paidSubscriptionRequest.user_id,
            paidSubscriptionRequest.owner_id,
            paidSubscriptionRequest.subscription_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val paidSubscriptionResponse = response as BaseResponse
                printStrJson(paidSubscriptionResponse, apiName)
                paidSubscription.value = paidSubscriptionResponse
            }
        }))
        return paidSubscription
    }


    fun stateList(
        progressShow: Boolean,
    ): MutableLiveData<StateCityResponse> {
        val apiName = APIServices.CANCEL_PAID_SUBSCRIPTION
        val stateCityResponseData: MutableLiveData<StateCityResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        val baseRequest = BaseRequest()
        baseRequest.user_id = Common.getUserId()
        printStrRequestJson(baseRequest, apiName)
        apiServices?.stateList(
            baseRequest.user_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val stateCityResponse = response as StateCityResponse
                printStrJson(stateCityResponse, apiName)
                stateCityResponseData.value = stateCityResponse
            }
        }))
        return stateCityResponseData
    }

    fun cityList(
        cityRequest: CityRequest,
        progressShow: Boolean,
    ): MutableLiveData<StateCityResponse> {
        val apiName = APIServices.CANCEL_PAID_SUBSCRIPTION
        val stateCityResponseData: MutableLiveData<StateCityResponse> =
            MutableLiveData()
        val apiServices: APIServices? = RetrofitService.createService(APIServices::class.java)
        cityRequest.user_id = Common.getUserId()
        printStrRequestJson(cityRequest, apiName)
        apiServices?.cityList(
            cityRequest.user_id, cityRequest.state_id
        )?.enqueue(CustomCB(progressShow, object : CustomCB.OnAPIResponse {
            override fun onResponse(response: Any) {
                val stateCityResponse = response as StateCityResponse
                printStrJson(stateCityResponse, apiName)
                stateCityResponseData.value = stateCityResponse
            }
        }))
        return stateCityResponseData
    }

    private fun printStrRequestJson(request: Any, apiName: String) {
        try {
            val json = Gson().toJson(request)
            if (BuildConfig.DEBUG) {
                Log.d("API---Request---$apiName", json)
            }
        } catch (e: Exception) {
        }
    }

    private fun printStrJson(response: Any, apiName: String) {
        try {
            val json = Gson().toJson(response)
            if (BuildConfig.DEBUG) {
                Log.d("API---Response---$apiName", json)
            }
        } catch (e: Exception) {
        }
    }
}
