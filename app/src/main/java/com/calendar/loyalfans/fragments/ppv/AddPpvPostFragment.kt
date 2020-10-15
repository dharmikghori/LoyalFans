package com.calendar.loyalfans.fragments.ppv

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.activities.PPVActivity
import com.calendar.loyalfans.adapter.SelectedFileAdapter
import com.calendar.loyalfans.dialog.FansSelectionDialog
import com.calendar.loyalfans.model.SelectedFileData
import com.calendar.loyalfans.model.response.FansData
import com.calendar.loyalfans.retrofit.APIServices
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.ImageSaver
import com.calendar.loyalfans.utils.SPHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_add_ppv_post.*
import kotlinx.android.synthetic.main.fragment_addpost.btnAddPost
import kotlinx.android.synthetic.main.fragment_addpost.etPostMessage
import kotlinx.android.synthetic.main.fragment_addpost.rvSelectedFiles
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.util.concurrent.TimeUnit


class AddPpvPostFragment : Fragment(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    companion object {
        fun newInstance() = AddPpvPostFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_add_ppv_post, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.ppv_send)
        setUpSelectedFilesAdapter()
        btnAddPost.setOnClickListener(this)
        imgBack.setOnClickListener(this)
        rgPPVType.setOnCheckedChangeListener(this)
        rbFree.isChecked = true
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
                if (activity is PPVActivity) {
                    val mainActivity = activity as PPVActivity
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

    override fun onClick(view: View?) {
        if (view != null) {
            if (view.id == R.id.btnAddPost && checkValidation()) {
                openFansSelectionDialog()
            } else if (view.id == R.id.imgBack) {
                activity?.onBackPressed()
            }
        }
    }

    private fun moveToHome() {
        activity?.runOnUiThread {
            selectedFileList.clear()
            selectedFileAdapter.notifyDataSetChanged()
            activity?.let { Common.showToast(it, "PPV Successfully Send") }
//            (activity as PPVActivity).loadFragment(15)
            requireActivity().onBackPressed()
        }
    }

    private fun openFansSelectionDialog() {
        val fansSelectionDialog = FansSelectionDialog(BaseActivity.getActivity())
        fansSelectionDialog.setOnPPVSend(object : FansSelectionDialog.OnPPVSend {
            override fun onSendPPV(selectedFans: List<FansData>) {
                callSendPPVAPI(selectedFans)
            }
        })
        fansSelectionDialog.show()
    }

    private fun callSendPPVAPI(selectedFans: List<FansData>) {
        val selectedFansId = ArrayList<String>()
        for (selectData in selectedFans) {
            selectedFansId.add(selectData.fanid)
        }
        val selectedFansIds = Gson().toJson(selectedFansId)
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
                    .addFormDataPart("ppv_type", when (rgPPVType.checkedRadioButtonId) {
                        R.id.rbPaid -> {
                            "PAID"
                        }
                        R.id.rbFree -> {
                            "FREE"
                        }
                        else -> ""
                    })
                    .addFormDataPart("amount", when (rgPPVType.checkedRadioButtonId) {
                        R.id.rbPaid -> {
                            etPPVPostAmount.text.toString()
                        }
                        R.id.rbFree -> {
                            "0"
                        }
                        else -> "0"
                    })
                    .addFormDataPart("ppv_send", selectedFansIds)
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
                    .url(APIServices.SERVICE_URL + APIServices.CREATE_PPV)
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


    private fun checkValidation(): Boolean {
        val contentString = etPostMessage.text.toString()
        if (contentString.isEmpty()) {
            activity?.let { Common.showToast(it, getString(R.string.content_validation)) }
            return false
        } else if (selectedFileList.size == 1) {
            activity?.let { Common.showToast(it, getString(R.string.attachment_validation)) }
            return false
        } else if (etPPVPostAmount.text.toString()
                .isEmpty() || etPPVPostAmount.text.toString() == "0"
        ) {
            activity?.let { Common.showToast(it, getString(R.string.ppv_price_validation)) }
            return false
        }
        return true
    }

    override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
        if (p1 == R.id.rbFree) {
            layPPVAmount.visibility = View.GONE
        } else if (p1 == R.id.rbPaid) {
            layPPVAmount.visibility = View.VISIBLE
        }
    }

}