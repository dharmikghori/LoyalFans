package com.calendar.loyalfans.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.SelectedFileData
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.layout_selected_file.view.*
import java.util.*

class SelectedFileAdapter(
    private var seletedImageList: ArrayList<SelectedFileData>,
    private val activity: FragmentActivity?,
) :
    RecyclerView.Adapter<SelectedFileAdapter.SelectedFileViewHolder>() {

    override fun getItemCount(): Int {
        return seletedImageList.size
    }

    private var onAddImage: OnAddImage? = null
    fun setOnAddImageClick(onAddImage: OnAddImage) {
        this.onAddImage = onAddImage
    }

    interface OnAddImage {
        fun onAddImageClick()
    }

    private fun getItem(position: Int): SelectedFileData {
        return seletedImageList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedFileViewHolder {
        return SelectedFileViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.layout_selected_file,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SelectedFileViewHolder, position: Int) {
        val selectedImageData = getItem(position)
        if (activity != null && selectedImageData.thumbnailPath.isNotEmpty()) {
            val bitmapFromImagePath =
                Common.getBitmapFromImagePath(activity, selectedImageData.thumbnailPath)
            holder.imgSelectedImage.setImageBitmap(bitmapFromImagePath)
        }

        holder.imgSelectedImage.setOnClickListener {
            if (position == 0) {
                onAddImage?.onAddImageClick()
            }
        }
    }

    class SelectedFileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgSelectedImage: ImageView = view.imgSelectedImage
    }


}


