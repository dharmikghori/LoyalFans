package com.calendar.loyalfans.fragments.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.adapter.CustomDropDownAdapter
import com.calendar.loyalfans.model.request.AddCardRequest
import com.calendar.loyalfans.model.response.CardData
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
        val view = inflater.inflate(R.layout.fragment_addcard, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.add_card)
        imgBack.setOnClickListener(this)
        btnSaveAddCard.setOnClickListener(this)
        getCountryByAPI()
        getCards()
    }

    private fun getCountryByAPI() {
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.countryList(false
        ).observe(viewLifecycleOwner, {
            if (it.status) {
                manageCountrySpinner(it.data)
            }
        })
    }

    private fun manageCountrySpinner(data: ArrayList<StateCityData>) {
        spCountryCard.adapter = activity?.let { CustomDropDownAdapter(it, data) }

    }

    private fun getCards() {
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.myCards(false
        ).observe(viewLifecycleOwner, {
            if (it.status && it.card_status) {
                setupCard(it.data)
            }
        })
    }

    private fun setupCard(cardData: CardData) {
        layCards.visibility = View.VISIBLE
        tvNameOnCard.text = cardData.name
        tvCardType.text = cardData.payment_method
        tvLastDigit.text = cardData.last4
    }

//    private fun getStates() {
//        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
//        baseViewModel.countryList(false
//        ).observe(viewLifecycleOwner, {
//            if (it.status) {
//                manageStateSpinner(it.data)
//            }
//        })
//
//    }

//    private fun manageStateSpinner(data: ArrayList<StateCityData>) {
//        spStateCard.adapter = activity?.let { CustomDropDownAdapter(it, data) }
//        spStateCard.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long,
//            ) {
//                val filter =
//                    data.filter { it.name == (spStateCard.selectedItem as StateCityData).name }
//                if (filter.isNotEmpty()) {
//                    val stateCityData = filter[0]
//                    bindCityBasedOnStates(stateCityData.id)
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//
//        }
//    }

//    private fun bindCityBasedOnStates(stateID: String) {
//        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
//        val cityRequest = CityRequest(stateID)
//        baseViewModel.cityList(cityRequest, false
//        ).observe(viewLifecycleOwner, {
//            if (it.status) {
//                manageCitySpinner(it.data)
//            }
//        })
//
//    }

//    private fun manageCitySpinner(data: ArrayList<StateCityData>) {
//        spCityCard.adapter = activity?.let { CustomDropDownAdapter(it, data) }
//    }


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
            var carNumber = etCardNumberCard.text.toString()
            carNumber = carNumber.replace(" ", "")
            val addCardRequest =
                AddCardRequest(
                    etStreetCard.text.toString(),
                    etCityCard.text.toString(),
                    etStateCard.text.toString(),
                    etZipCodeCard.text.toString(),
                    (spCountryCard.selectedItem as StateCityData).code,
                    etNameOnCardCard.text.toString(),
                    carNumber,
                    etCardExpYearCard.text.toString(),
                    etExpMonthCard.text.toString(),
                    etCardCVVCard.text.toString()
                )
            val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
            baseViewModel.addCard(addCardRequest, true
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
            etStateCard.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.state_validation)) }
                return false
            }
            etCityCard.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.city_validation)) }
                return false
            }
            etNameOnCardCard.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.card_name_validation)) }
                return false
            }
            etCardNumberCard.text?.isEmpty()!! -> {
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


//    private val CARD_NUMBER_TOTAL_SYMBOLS = 19 // size of pattern 0000-0000-0000-0000
//    private val CARD_NUMBER_TOTAL_DIGITS = 16 // max numbers of digits in pattern: 0000 x 4
//    private val CARD_NUMBER_DIVIDER_MODULO =
//        5 // means divider position is every 5th symbol beginning with 1
//    private val CARD_NUMBER_DIVIDER_POSITION =
//        CARD_NUMBER_DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0
//    private val CARD_NUMBER_DIVIDER = '-'


//    private fun isInputCorrect(
//        s: Editable,
//        size: Int,
//        dividerPosition: Int,
//        divider: Char,
//    ): Boolean {
//        var isCorrect = s.length <= size
//        for (i in s.indices) {
//            isCorrect = if (i > 0 && (i + 1) % dividerPosition == 0) {
//                isCorrect and (divider == s[i])
//            } else {
//                isCorrect and Character.isDigit(s[i])
//            }
//        }
//        return !isCorrect
//    }
//
//    private fun concatString(digits: CharArray, dividerPosition: Int, divider: Char): String {
//        val formatted = StringBuilder()
//        for (i in digits.indices) {
//            if (i != 0) {
//                formatted.append(digits[i])
//                if (i > 0 && i < digits.size - 1 && (i + 1) % dividerPosition == 0) {
//                    formatted.append(divider)
//                }
//            }
//        }
//        return formatted.toString()
//    }
//
//    private fun getDigitArray(s: Editable, size: Int): CharArray {
//        val digits = CharArray(size)
//        var index = 0
//        var i = 0
//        while (i < s.length && index < size) {
//            val current = s[i]
//            if (Character.isDigit(current)) {
//                digits[index] = current
//                index++
//            }
//            i++
//        }
//        return digits
//    }


}