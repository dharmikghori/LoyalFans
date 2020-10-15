package com.calendar.loyalfans.model.response


open class PpvHistoryResponse : BaseResponse() {
    lateinit var data: PpvHistoryResponse
    lateinit var my_ppvs: ArrayList<MyPPVData>
    lateinit var other_ppvs: ArrayList<OtherPPVData>
}

open class MyPPVData : BaseResponse() {
    var id: String = ""
    var user_id: String = ""
    var created_at: String = ""
    var details: MyPPVDetailsData? = null
    var analytics: MyPPVAnalyticData? = null
}

open class MyPPVDetailsData {
    var id: String = ""
    var user_id: String = ""
    var content: String = ""
    var type: String = ""
    var file_types: String = ""
    var price: String = ""
    var status: String = ""
    var updated_at: String = ""
    var created_at: String = ""
    var paid: String = ""
    var files: ArrayList<MyPPVFilesData> = ArrayList()
}


open class MyPPVFilesData {
    var id: String = ""
    var ppv_id: String = ""
    var file_name: String = ""
    var type: String = ""
}

open class MyPPVAnalyticData {
    var earning: EarningData? = null
    var followers: ArrayList<FollowersData> = ArrayList()
    var seen: ArrayList<SeenData> = ArrayList()
}

open class EarningData {
    var seen: String = "0"
    var send: String = "0"
    var earning: String = "0"
}

open class FollowersData {
    var id: String = ""
    var username: String = ""
    var display_name: String = ""
    var profile_img: String = ""
    var sended: String = ""
}

open class SeenData {
    var id: String = ""
    var username: String = ""
    var display_name: String = ""
    var profile_img: String = ""
}


open class OtherPPVData : BaseResponse() {
    var id: String = ""
    var uid: String = ""
    var username: String = ""
    var display_name: String = ""
    var profile_img: String = ""
    var unseen: String = "0"
    var details: ArrayList<MyPPVDetailsData>? = null
}


