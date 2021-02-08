package com.supernebr.utils

interface Constants {
    companion object {

        //Local server url
        const val BASE_URL = "http://demo1929804.mockable.io/"

        //Live server url
        //val BASE_URL = "http://supernebr.com/supernebr/api/"

        var list:ArrayList<String> = ArrayList<String>()
        var group_id:String=""

        var user_name:String=""
        var password:String=""
        val NAME_REGEX = ".{2,}"
        val PASSWORD_REGEX = ".{3,}"
        val EMPTY_REGEX = ".{1,}"

        val IS_NOTIFICATION = "is_notification"
    }

}