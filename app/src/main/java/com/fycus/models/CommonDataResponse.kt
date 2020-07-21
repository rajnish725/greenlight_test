package com.fycus.models

import com.google.gson.annotations.SerializedName

open class CommonDataResponse<any> {

    @SerializedName("data")
    val mData: any? = null

    val status: Int? = null
    val message: String? = null
    val responseStatus: String? = null
}


