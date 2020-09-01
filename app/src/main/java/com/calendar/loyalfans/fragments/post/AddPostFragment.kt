package com.calendar.loyalfans.fragments.post

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.calendar.loyalfans.R
import com.calendar.loyalfans.adapter.SelectedFileAdapter
import com.calendar.loyalfans.model.SelectedFileData
import com.calendar.loyalfans.ui.BaseActivity
import com.calendar.loyalfans.ui.MainActivity
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.ImageSaver
import kotlinx.android.synthetic.main.fragment_addpost.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*

class AddPostFragment : Fragment() {

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
                        ) {
                            manageSelectedMediaDataAndThumbnail(bitmap, imagePath)
                        }
                    })
                }
            }
        })
    }

    private fun manageSelectedMediaDataAndThumbnail(bitmap: Bitmap?, imagePath: String?) {
        var thumbImagePath: String
        val selectedFileData = SelectedFileData()
        if (imagePath != null && Common.isVideo(imagePath)) {
            ImageSaver(activity).saveFileIntoPrivateStorage(
                imagePath
            ) { destinationPath, error ->
                thumbImagePath = ImageSaver.GetThumbNameFromFileName(
                    destinationPath
                )
                selectedFileData.thumbnailPath = thumbImagePath
                selectedFileData.filePath = imagePath
                updateAdapterAndData(selectedFileData)
            }
        } else {
            selectedFileData.imageBitmap = bitmap
            if (imagePath != null) {
                selectedFileData.thumbnailPath = imagePath
                selectedFileData.filePath = imagePath
            }
            updateAdapterAndData(selectedFileData)
        }
    }

    private fun updateAdapterAndData(selectedFileData: SelectedFileData) {
        selectedFileList.add(selectedFileData)
        selectedFileAdapter.notifyDataSetChanged()
        rvSelectedFiles.smoothScrollToPosition(selectedFileList.size - 1)
    }

}