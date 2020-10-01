package com.calendar.loyalfans.fragments.payment

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.activities.MainActivity
import com.calendar.loyalfans.adapter.CustomDropDownAdapter
import com.calendar.loyalfans.model.SelectedFileData
import com.calendar.loyalfans.model.request.CityRequest
import com.calendar.loyalfans.model.response.StateCityData
import com.calendar.loyalfans.retrofit.APIServices
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.SPHelper
import kotlinx.android.synthetic.main.fragment_bank_detail.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.tvToolBarName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class BankFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = BankFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_bank_detail, container, false)
    }

    var selectedSelfieImage: SelectedFileData? = null
    var selectedIDImage: SelectedFileData? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.banking)
        imgBack.setOnClickListener(this)
        btnChooseID.setOnClickListener(this)
        btnChooseSelfie.setOnClickListener(this)
        btnSubmitBankDetails.setOnClickListener(this)
        btnDateOfBirth.setOnClickListener(this)
        btnIDExpirationDate.setOnClickListener(this)
        getStates()
        setUpDocumentTypeSpinner()
    }

    private fun setUpDocumentTypeSpinner() {

        spDocumentSelectionBank.adapter =
            activity?.let { CustomDropDownAdapter(it, getDocumentList()) }

    }

    private fun getDocumentList(): ArrayList<StateCityData> {

        val documentList = ArrayList<StateCityData>()

        val stateCityItem = StateCityData()
        stateCityItem.name = getString(R.string.passport)
        documentList.add(stateCityItem)

        val stateCityItem1 = StateCityData()
        stateCityItem1.name = getString(R.string.driver_licence)
        documentList.add(stateCityItem1)

        val stateCityItem2 = StateCityData()
        stateCityItem2.name = getString(R.string.state_id_card)
        documentList.add(stateCityItem2)
        return documentList
    }

    private fun getStates() {
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.stateList(false
        ).observe(viewLifecycleOwner, {
            if (it.status) {
                manageStateSpinner(it.data)
            }
        })

    }

    private fun manageStateSpinner(data: ArrayList<StateCityData>) {
        spStateBank.adapter = activity?.let { CustomDropDownAdapter(it, data) }
        spStateBank.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val filter =
                    data.filter { it.name == (spStateBank.selectedItem as StateCityData).name }
                if (filter.isNotEmpty()) {
                    val stateCityData = filter[0]
                    bindCityBasedOnStates(stateCityData.id)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    private fun bindCityBasedOnStates(stateID: String) {
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        val cityRequest = CityRequest(stateID)
        baseViewModel.cityList(cityRequest, false
        ).observe(viewLifecycleOwner, {
            if (it.status) {
                manageCitySpinner(it.data)
            }
        })

    }

    private fun manageCitySpinner(data: ArrayList<StateCityData>) {
        spCityBank.adapter = activity?.let { CustomDropDownAdapter(it, data) }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.imgBack -> {
                    activity?.onBackPressed()
                }
                R.id.btnChooseSelfie -> {
                    if (activity is MainActivity) {
                        val mainActivity = activity as MainActivity
                        mainActivity.imageSelection()
                        mainActivity.setOnImageSelection(object : BaseActivity.OnImageSelection {
                            override fun onSuccess(
                                bitmap: Bitmap?,
                                imagePath: String?,
                                imageUri: Uri?,
                            ) {
                                selectedSelfieImage = SelectedFileData()
                                selectedSelfieImage!!.imageUri = imageUri
                                if (imagePath != null) {
                                    selectedSelfieImage!!.filePath = imagePath
                                    btnChooseSelfie.text =
                                        getString(R.string.choose_file) + " - " + File(imagePath).name
                                }
                            }
                        })
                    }
                }
                R.id.btnChooseID -> {
                    if (activity is MainActivity) {
                        val mainActivity = activity as MainActivity
                        mainActivity.imageSelection()
                        mainActivity.setOnImageSelection(object : BaseActivity.OnImageSelection {
                            override fun onSuccess(
                                bitmap: Bitmap?,
                                imagePath: String?,
                                imageUri: Uri?,
                            ) {

                                selectedIDImage = SelectedFileData()
                                selectedIDImage!!.imageUri = imageUri
                                if (imagePath != null) {
                                    selectedIDImage!!.filePath = imagePath
                                    btnChooseID.text =
                                        getString(R.string.choose_file) + " - " + File(imagePath).name
                                }
                            }
                        })
                    }
                }
                R.id.btnSubmitBankDetails -> {
                    makeAddBankDetailsCall()
                }
                R.id.btnIDExpirationDate -> {
                    openDatePicker(btnIDExpirationDate)
                }
                R.id.btnDateOfBirth -> {
                    openDatePicker(btnDateOfBirth)
                }
            }
        }
    }

    private fun openDatePicker(buttonDate: Button?) {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        val dpd = activity?.let {
            DatePickerDialog(it,
                { view, year, monthOfYear, dayOfMonth -> // Display Selected date in textbox
                    var actualMonth = "0"
                    var actualDay = "0"
                    actualMonth = if ((monthOfYear + 1) < 10) {
                        "0${monthOfYear + 1}"
                    } else {
                        (monthOfYear + 1).toString()
                    }
                    actualDay = if (dayOfMonth < 10) {
                        "0$dayOfMonth "
                    } else {
                        dayOfMonth.toString()
                    }
                    buttonDate?.text =
                        ("$year-$actualMonth-$actualDay")
                }, year, month, day)
        }
        dpd?.show()

    }

    private fun makeAddBankDetailsCall() {
        if (checkValidation()) {
            Common.displayProgress(BaseActivity.getActivity())
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.Default) {
                    val client = OkHttpClient()
                        .newBuilder()
                        .connectTimeout(10, TimeUnit.MINUTES)
                        .writeTimeout(10, TimeUnit.MINUTES)
                        .readTimeout(10, TimeUnit.MINUTES)
                        .build()

                    var body: MultipartBody.Builder =
                        MultipartBody.Builder().setType(MultipartBody.FORM)
                    body.addFormDataPart("first_name", etFirstNameBank.text.toString())
                        .addFormDataPart("last_name", etLastNameBank.text.toString())
                        .addFormDataPart("phone", etPhoneBank.text.toString())
                        .addFormDataPart("ssn_num", etTaxIDBank.text.toString())
                        .addFormDataPart("country", etCountryNameBank.text.toString())
                        .addFormDataPart("street", etStreetBank.text.toString())
                        .addFormDataPart("state", (spStateBank.selectedItem as StateCityData).name)
                        .addFormDataPart("zip_code", etZipCodeBank.text.toString())
                        .addFormDataPart("birthdate", btnDateOfBirth.text.toString())
                        .addFormDataPart("document_type",
                            (spDocumentSelectionBank.selectedItem as StateCityData).name)
                        .addFormDataPart("exp_date", btnIDExpirationDate.text.toString())
                        .addFormDataPart("user_id", Common.getUserId())
                        .addFormDataPart("city", (spCityBank.selectedItem as StateCityData).name)
                    body = selectedSelfieImage?.let { manageDocumentData(body, it, "doc_selfi") }!!
                    body = selectedIDImage?.let { manageDocumentData(body, it, "doc_id_pic") }!!

                    val build = body.build()
                    val request: Request = Request.Builder()
                        .url(APIServices.SERVICE_URL + APIServices.ADD_BANK)
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
                            activity?.let {
                                Common.showToast(it,
                                    getString(R.string.unable_post_bank))
                            }
                        }
                    } catch (e: Exception) {
                        Common.dismissProgress()
                        activity?.let { e.message?.let { it1 -> Common.showToast(it, it1) } }
                    }
                }
            }
        }
    }

    private fun manageDocumentData(
        body: MultipartBody.Builder,
        selectedFileData: SelectedFileData,
        bodyName: String,
    ): MultipartBody.Builder {
        val file = File(selectedFileData.filePath)
        val fileUri = when (selectedFileData.imageUri) {
            null -> {
                Uri.fromFile(file)
            }
            else -> {
                selectedFileData.imageUri
            }
        }

        body.addFormDataPart(bodyName, file.name,
            RequestBody.create(
                fileUri?.let {
                    BaseActivity.getActivity().contentResolver.getType(
                        it).toString().toMediaTypeOrNull()
                },
                file
            ))
        return body
    }

    private fun moveToHome() {
        activity?.runOnUiThread {
            activity?.let { Common.showToast(it, getString(R.string.bank_details_added)) }
            (activity as MainActivity).loadFragment(1)
        }
    }

    private fun checkValidation(): Boolean {
        when {
            etFirstNameBank.text.toString().isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.firstname_bank)) }
                return false
            }
            etLastNameBank.text.toString().isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.lastname_bank)) }
                return false
            }
            etPhoneBank.text.toString().isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.phone_bank)) }
                return false
            }
            etTaxIDBank.text.toString().isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.taxid_validation)) }
                return false
            }
            etCountryNameBank.text.toString().isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.country_validation)) }
                return false
            }
            etStreetBank.text.toString().isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.street_validation)) }
                return false
            }
//            (spStateBank.selectedItem.toString() == "-- Select --") -> {
//                activity?.let { Common.showToast(it, getString(R.string.state_validation)) }
//                return false
//            }
//            (spCityBank.selectedItem.toString() == "-- Select --") -> {
//                activity?.let { Common.showToast(it, getString(R.string.city_validation)) }
//                return false
//            }
            etZipCodeBank.text.toString().isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.zipcode_validation)) }
                return false
            }
            btnDateOfBirth.text.toString().isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.dob_validation)) }
                return false
            }
            selectedSelfieImage == null -> {
                activity?.let { Common.showToast(it, getString(R.string.selfie_image_validation)) }
                return false
            }
            selectedIDImage == null -> {
                activity?.let { Common.showToast(it, getString(R.string.id_image_validation)) }
                return false
            }
            btnIDExpirationDate.text.toString().isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.id_expiration_validation)) }
                return false
            }
        }
        return true
    }


}