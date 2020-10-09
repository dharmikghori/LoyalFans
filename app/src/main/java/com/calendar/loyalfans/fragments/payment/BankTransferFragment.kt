package com.calendar.loyalfans.fragments.payment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.MainActivity
import com.calendar.loyalfans.activities.WebViewActivity
import com.calendar.loyalfans.adapter.CustomDropDownAdapter
import com.calendar.loyalfans.model.request.BankTransferRequest
import com.calendar.loyalfans.model.response.StateCityData
import com.calendar.loyalfans.retrofit.APIServices
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.RequestParams
import kotlinx.android.synthetic.main.fragment_bank_transfer.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.tvToolBarName


class BankTransferFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = BankTransferFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_bank_transfer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvToolBarName.text = getString(R.string.bank_transfer)
        imgBack.setOnClickListener(this)
        btnSubmitPayoutDetails.setOnClickListener(this)
        setUpAccountTypeSpinner()
        setUpClickHere(clickHereToDownloadW9,
            "To Download W9 Form <a href='clickhere'>Click Here</a>")
        setUpClickHere(clickHereToEditW9,
            "To Edit W9 Form <a href='clickhere'>Click Here</a>")
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.imgBack -> {
                    activity?.onBackPressed()
                }
                R.id.tvW9Form -> {
                    (activity as MainActivity).loadFragment(20)
                }
                R.id.tvBankTransfer -> {
                    (activity as MainActivity).loadFragment(21)
                }
                R.id.btnSubmitPayoutDetails -> {
                    onSubmit()
                }
            }
        }
    }

    private fun onSubmit() {
        if (checkValidation()) {
            val bankTransferRequest =
                BankTransferRequest(
                    etRoutingNumberBankTransfer.text.toString(),
                    etAccountNumberBankTransfer.text.toString(),
                    (spAccountType.selectedItem as StateCityData).name,
                    getString(R.string.us), etFirstNameBankTransfer.text.toString(),
                    etLastNameBankTransfer.text.toString(),
                    etBusinessNameBankTransfer.text.toString(), etEmailBankTransfer.text.toString()
                )
            val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
            baseViewModel.bankTransfer(
                bankTransferRequest, true
            ).observe(this, {
                if (it.status) {
                    activity?.let { it1 -> Common.showToast(it1, it.msg) }
                    activity?.onBackPressed()
                }
            })
        }
    }

    private fun checkValidation(): Boolean {
        when {
            etRoutingNumberBankTransfer.text.isEmpty() -> {
                activity?.let {
                    Common.showToast(it,
                        getString(R.string.routing_number_validation))
                }
                return false
            }
            etAccountNumberBankTransfer.text.isEmpty() -> {
                activity?.let {
                    Common.showToast(it,
                        getString(R.string.account_number_validation))
                }
                return false
            }
            etFirstNameBankTransfer.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.firstname_validation)) }
                return false
            }
            etLastNameBankTransfer.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.lastname_validation)) }

                return false
            }
            etEmailBankTransfer.text.isEmpty() -> {
                activity?.let { Common.showToast(it, getString(R.string.email_validation)) }
                return false
            }
        }
        return true
    }

    private fun setUpClickHere(textView: TextView, textForTextView: String) {
        val sequence: CharSequence =
            Html.fromHtml(textForTextView)
        val strBuilder = SpannableStringBuilder(sequence)
        val urls: Array<URLSpan> = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {
            makeLinkClickable(strBuilder, span)
        }
        textView.text = strBuilder
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                if (strBuilder.contains("Download")) {
//                    startActivity(Intent(activity,
//                        WebViewActivity::class.java).putExtra(RequestParams.WEBVIEW_URL,
//                        APIServices.W9_FORM_DOWNLOAD_URL + Common.getUserId()))
//
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(APIServices.W9_FORM_DOWNLOAD_URL + Common.getUserId())
                    startActivity(i)
                } else if (strBuilder.contains("Edit")) {
                    startActivity(Intent(activity,
                        WebViewActivity::class.java).putExtra(RequestParams.WEBVIEW_URL,
                        APIServices.W9_FORM_WEB_URL + Common.getUserId()))
                }
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }

    private fun setUpAccountTypeSpinner() {
        spAccountType.adapter =
            activity?.let { CustomDropDownAdapter(it, getAccountTypeList()) }

    }

    private fun getAccountTypeList(): ArrayList<StateCityData> {
        val documentList = ArrayList<StateCityData>()

        val stateCityItem = StateCityData()
        stateCityItem.name = getString(R.string.individual)
        documentList.add(stateCityItem)

        val stateCityItem1 = StateCityData()
        stateCityItem1.name = getString(R.string.company)
        documentList.add(stateCityItem1)
        return documentList
    }

//    class DownloadTask(context1: Context, downloadUrl: String) {
//        private val context = context1
//        private val downloadUrl = ""
//        private var downloadFileName = ""
//        private var progressDialog: ProgressDialog? = null
//
//        private open inner class DownloadingTask :
//            AsyncTask<Void?, Void?, Void?>() {
//            var apkStorage: File? = null
//            var outputFile: File? = null
//            protected override fun onPreExecute() {
//                super.onPreExecute()
//                progressDialog = ProgressDialog(context)
//                progressDialog!!.setMessage("Downloading...")
//                progressDialog!!.setCancelable(false)
//                progressDialog!!.show()
//            }
//
//            protected fun onPostExecute(result: Void?) {
//                try {
//                    if (outputFile != null) {
//                        progressDialog?.dismiss()
//                        val ctw = ContextThemeWrapper(context, R.style.Theme_AlertDialog)
//                        val alertDialogBuilder: AlertDialog.Builder = Request.Builder(ctw)
//                        alertDialogBuilder.setTitle("Document  ")
//                        alertDialogBuilder.setMessage("Document Downloaded Successfully ")
//                        alertDialogBuilder.setCancelable(false)
//                        alertDialogBuilder.setPositiveButton("ok",
//                            object : DialogInterface.OnClickListener() {
//                                fun onClick(dialog: DialogInterface?, id: Int) {}
//                            })
//                        alertDialogBuilder.setNegativeButton("Open report",
//                            object : OnClickListener() {
//                                fun onClick(dialog: DialogInterface?, id: Int) {
//                                    val pdfFile = File(Environment.getExternalStorageDirectory()
//                                        .toString() + "/CodePlayon/" + downloadFileName) // -> filename = maven.pdf
//                                    val path: Uri = Uri.fromFile(pdfFile)
//                                    val pdfIntent = Intent(Intent.ACTION_VIEW)
//                                    pdfIntent.setDataAndType(path, "application/pdf")
//                                    pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                    try {
//                                        context.startActivity(pdfIntent)
//                                    } catch (e: ActivityNotFoundException) {
//                                        Toast.makeText(context,
//                                            "No Application available to view PDF",
//                                            Toast.LENGTH_SHORT).show()
//                                    }
//                                }
//                            })
//                        alertDialogBuilder.show()
//                        //                    Toast.makeText(context, "Document Downloaded Successfully", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Handler().postDelayed(Runnable { }, 3000)
//                        Log.e(TAG, "Download Failed")
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//
//                    //Change button text if exception occurs
//                    Handler().postDelayed(Runnable { }, 3000)
//                    Log.e(TAG, "Download Failed with Exception - " + e.localizedMessage)
//                }
//                super.onPostExecute(result)
//            }
//
//            protected fun doInBackground(vararg arg0: Void?): Void? {
//                try {
//                    val url = URL(downloadUrl) //Create Download URl
//                    val c: HttpURLConnection =
//                        url.openConnection() as HttpURLConnection //Open Url Connection
//                    c.setRequestMethod("GET") //Set Request Method to "GET" since we are grtting data
//                    c.connect() //connect the URL Connection
//
//                    //If Connection response is not OK then show Logs
//                    if (c.getResponseCode() !== HttpURLConnection.HTTP_OK) {
//                        Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
//                            .toString() + " " + c.getResponseMessage())
//                    }
//
//
//                    //Get File if SD card is present
//                    if (CheckForSDCard().isSDCardPresent()) {
//                        apkStorage = File(Environment.getExternalStorageDirectory()
//                            .toString() + "/" + "CodePlayon")
//                    } else Toast.makeText(context,
//                        "Oops!! There is no SD Card.",
//                        Toast.LENGTH_SHORT).show()
//
//                    //If File is not present create directory
//                    if (!apkStorage.exists()) {
//                        apkStorage.mkdir()
//                        Log.e(TAG, "Directory Created.")
//                    }
//                    outputFile =
//                        File(apkStorage, downloadFileName) //Create Output file in Main File
//
//                    //Create New File if not present
//                    if (!outputFile.exists()) {
//                        outputFile.createNewFile()
//                        Log.e(TAG, "File Created")
//                    }
//                    val fos = FileOutputStream(outputFile) //Get OutputStream for NewFile Location
//                    val `is`: InputStream = c.getInputStream() //Get InputStream for connection
//                    val buffer = ByteArray(1024) //Set buffer type
//                    var len1 = 0 //init length
//                    while (`is`.read(buffer).also { len1 = it } != -1) {
//                        fos.write(buffer, 0, len1) //Write new file
//                    }
//
//                    //Close all connection after doing task
//                    fos.close()
//                    `is`.close()
//                } catch (e: Exception) {
//
//                    //Read exception if something went wrong
//                    e.printStackTrace()
//                    outputFile = null
//                    Log.e(TAG, "Download Error Exception " + e.message)
//                }
//                return null
//            }
//        }
//
//        companion object {
//            private const val TAG = "Download Task"
//        }
//
//        init {
//            this.context = context
//            this.downloadUrl = downloadUrl
//            downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'),
//                downloadUrl.length) //Create file name by picking download file name from URL
//            Log.e(TAG, downloadFileName)
//
//            //Start Downloading Task
//            DownloadingTask().execute()
//        }
//    }


}