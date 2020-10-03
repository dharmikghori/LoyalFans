package com.calendar.loyalfans.fragments.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.adapter.CustomDropDownAdapter
import com.calendar.loyalfans.model.request.AddCardRequest
import com.calendar.loyalfans.model.request.CityRequest
import com.calendar.loyalfans.model.response.StateCityData
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import kotlinx.android.synthetic.main.fragment_addcard.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.tvToolBarName
import java.util.*

class AddCardFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = AddCardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_addcard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.add_card)
        imgBack.setOnClickListener(this)
        btnSaveAddCard.setOnClickListener(this)
        getStates()
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
        spStateCard.adapter = activity?.let { CustomDropDownAdapter(it, data) }
        spStateCard.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val filter =
                    data.filter { it.name == (spStateCard.selectedItem as StateCityData).name }
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
        spCityCard.adapter = activity?.let { CustomDropDownAdapter(it, data) }
    }


    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.imgBack -> {
                    activity?.onBackPressed()
                }
                R.id.btnSaveAddCard -> {
                    onSaveCard()
                }
            }
        }
    }

    private fun onSaveCard() {
        if (checkValidation()) {
            val addCardRequest =
                AddCardRequest(
                    etStreetCard.text.toString(),
                    (spCityCard.selectedItem as StateCityData).name,
                    (spStateCard.selectedItem as StateCityData).name,
                    etZipCodeCard.text.toString(),
                    getString(R.string.us),
                    etNameOnCardCard.text.toString(),
                    etCardNumberCard.text.toString(),
                    etCardExpYearCard.text.toString(),
                    etExpMonthCard.text.toString(),
                    etCardCVVCard.text.toString()
                )
            val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
            baseViewModel.addCard(addCardRequest, false
            ).observe(viewLifecycleOwner,
                {
                    if (it.status) {
                        Common.showToast(BaseActivity.getActivity(), it.msg)
                        activity?.onBackPressed()
                    }
                })
        }

    }

    private fun checkValidation(): Boolean {
        when {
            etStreetCard.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.street_validation)) }
                return false
            }
            etZipCodeCard.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.zipcode_validation)) }
                return false
            }
            etNameOnCardCard.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.card_name_validation)) }
                return false
            }
            etCardNumberCard.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.card_number_validation)) }
                return false
            }
            etExpMonthCard.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.expy_month_validation)) }
                return false
            }
            etCardExpYearCard.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.expy_year_validation)) }
                return false
            }
            (checkExpiryDateIsNotValid()) -> {
                return false
            }
            etCardCVVCard.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.card_cvv_validation)) }
                return false
            }
            else -> return true
        }
    }

    private fun checkExpiryDateIsNotValid(): Boolean {
        if (checkYearIsNotValid()) {
            activity?.let { Common.showToast(it, getString(R.string.year_validation)) }
            return true
        } else if (checkYearIsNotValid() && checkMonthIsNotValid()) {
            activity?.let { Common.showToast(it, getString(R.string.month_validation)) }
            return true
        }
        return false
    }

    private fun checkMonthIsNotValid(): Boolean {
        val cardExpiryMonth = etExpMonthCard.text.toString().toInt()
        val calendar = Calendar.getInstance()
        val currentMonth = calendar[Calendar.MONTH] + 1
        return currentMonth >= cardExpiryMonth
    }

    private fun checkYearIsNotValid(): Boolean {
        val cardExpiryYear = (etCardExpYearCard.text.toString()).toInt()
        val calendar = Calendar.getInstance()
        val currentYear = calendar[Calendar.YEAR]
        return currentYear > cardExpiryYear
    }


}