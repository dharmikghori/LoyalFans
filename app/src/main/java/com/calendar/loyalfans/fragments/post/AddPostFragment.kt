package com.calendar.loyalfans.fragments.post

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.activities.MainActivity
import com.calendar.loyalfans.adapter.SelectedFileAdapter
import com.calendar.loyalfans.model.SelectedFileData
import com.calendar.loyalfans.retrofit.APIServices
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.ImageSaver
import com.calendar.loyalfans.utils.SPHelper
import kotlinx.android.synthetic.main.fragment_addpost.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.util.concurrent.TimeUnit


class AddPostFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = AddPostFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_addpost, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.add_post)
        setUpSelectedFilesAdapter()
        imgBack.visibility = View.GONE
        btnAddPost.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).manageBottomNavigationVisibility(true)
    }
    private val selectedFileList = ArrayList<SelectedFileData>()
    private lateinit var selectedFileAdapter: SelectedFileAdapter
    private fun setUpSelectedFilesAdapter() {
        rvSelectedFiles?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true)
        if (selectedFileList.isEmpty()) {
            selectedFileList.add(SelectedFileData())
        }
        selectedFileAdapter = SelectedFileAdapter(selectedFileList, activity)
        rvSelectedFiles.adapter = selectedFileAdapter
        selectedFileAdapter.setOnAddImageClick(object : SelectedFileAdapter.OnAddImage {
            override fun onAddImageClick() {
                if (activity is MainActivity) {
                    val mainActivity = activity as MainActivity
                    mainActivity.imageAndVideoSelectionForPost()
                    mainActivity.setOnImageSelection(object : BaseActivity.OnImageSelection {
                        override fun onSuccess(
                            bitmap: Bitmap?,
                            imagePath: String?,
                            imageUri: Uri?,
                        ) {
                            manageSelectedMediaDataAndThumbnail(bitmap, imagePath, imageUri)
                        }
                    })
                }
            }

            override fun onRemoveImageClick(position: Int) {
                selectedFileList.removeAt(position)
                selectedFileAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun manageSelectedMediaDataAndThumbnail(
        bitmap: Bitmap?,
        imagePath: String?,
        imageUri: Uri?,
    ) {
        var thumbImagePath: String
        val selectedFileData = SelectedFileData()
        if (imagePath != null && Common.isVideo(imagePath)) {
            ImageSaver(context).saveFileIntoPrivateStorage(imagePath
            ) { destinationPath, error ->
                val getThumbNameFromFileName = ImageSaver.GetThumbNameFromFileName(destinationPath)
                selectedFileData.thumbnailPath = getThumbNameFromFileName
                selectedFileData.filePath = imagePath
                selectedFileData.imageUri = imageUri
                updateAdapterAndData(selectedFileData)
            }

        } else {
            selectedFileData.imageBitmap = bitmap
            if (imagePath != null) {
                selectedFileData.thumbnailPath = imagePath
                selectedFileData.filePath = imagePath
                selectedFileData.imageUri = imageUri
            }
            updateAdapterAndData(selectedFileData)
        }
    }

    private fun updateAdapterAndData(selectedFileData: SelectedFileData) {

        selectedFileList.add(selectedFileData)
        selectedFileAdapter.notifyDataSetChanged()
        rvSelectedFiles.smoothScrollToPosition(selectedFileList.size - 1)
    }

    override fun onClick(p0: View?) {
//        BaseActivity.getActivity().runOnUiThread {
        if (checkValidation()) {
            val contentString = etPostMessage.text.toString()
            val userId = Common.getUserId()
            var i = 1
            val size: Int = selectedFileList.size
            Common.displayProgress(BaseActivity.getActivity())
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    val client = OkHttpClient()
                        .newBuilder()
                        .connectTimeout(10, TimeUnit.MINUTES)
                        .writeTimeout(10, TimeUnit.MINUTES)
                        .readTimeout(10, TimeUnit.MINUTES)
                        .build()

                    val body: MultipartBody.Builder =
                        MultipartBody.Builder().setType(MultipartBody.FORM)
                    body.addFormDataPart("content", contentString)
                        .addFormDataPart("user_id", userId)
                    while (i < size) {
                        val selectedFileData = selectedFileList[i]
                        val file = File(selectedFileData.filePath)
                        val fileUri = when (selectedFileData.imageUri) {
                            null -> {
                                Uri.fromFile(file)
                            }
                            else -> {
                                selectedFileData.imageUri
                            }
                        }

                        body.addFormDataPart(i.toString(), file.name,
                            RequestBody.create(fileUri?.let {
                                BaseActivity.getActivity().contentResolver.getType(
                                    it).toString().toMediaTypeOrNull()
                            },
                                file
                            ))
                        i++
                    }
                    val build = body.build()
                    val request: Request = Request.Builder()
                        .url(APIServices.SERVICE_URL + APIServices.NEW_POST)
                        .method("POST", build)
                        .addHeader("App-Secret-Key",
                            SPHelper(BaseActivity.getActivity()).getLoginAppSecretKey())
                        .addHeader("Authorization_token",
                            "eyJ0eXA1iOi0JKV1QiL8CJhb5GciTWvLUzI1NiJ9IiRk2YXRh8Ig")
                        .addHeader("Authorization", "Basic YWRtaW46MTIzNA==")
                        .build()
                    try {
                        val response: Response = client.newCall(request).execute()
                        Common.dismissProgress()
                        if (response.code == 200) {
                            moveToHome()
                        } else {
                            activity?.let { Common.showToast(it, "Unable to add post") }
                        }
                    } catch (e: Exception) {
                        Common.dismissProgress()
                        activity?.let { e.message?.let { it1 -> Common.showToast(it, it1) } }
                    }
                }
            }
        }
//        }
    }

    private fun moveToHome() {
        activity?.runOnUiThread {
            selectedFileList.clear()
            selectedFileAdapter.notifyDataSetChanged()
            activity?.let { Common.showToast(it, "Post Added") }
            (activity as MainActivity).loadFragment(1)
            etPostMessage.setText("")
        }
    }

    private fun checkValidation(): Boolean {
        val contentString = etPostMessage.text.toString()
        if (contentString.isEmpty()) {
            activity?.let { Common.showToast(it, getString(R.string.content_validation)) }
            return false
        } else if (selectedFileList.size == 1) {
            activity?.let { Common.showToast(it, getString(R.string.attachment_validation)) }
            return false
        }
        return true
    }

}