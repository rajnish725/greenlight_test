package com.test.models

import com.google.gson.annotations.SerializedName

open class CommonDataResponse<any> {

    @SerializedName("ResponseData")
    val mData: any? = null

    val ResponseStatus: Int? = null
    val message: String? = null
    val Success: String? = null
}


