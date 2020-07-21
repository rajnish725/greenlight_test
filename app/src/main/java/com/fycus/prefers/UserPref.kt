package com.fycus.prefers

import android.content.Context
import android.content.SharedPreferences
import com.fycus.models.UserModel
import com.google.gson.Gson
import javax.inject.Singleton


@Singleton
class UserPref(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("userPref", Context.MODE_PRIVATE)
    private val gSon: Gson = Gson()

    var user: UserModel
        get() = gSon.fromJson<Any>(
            preferences.getString("user", null),
            UserModel::class.java
        ) as UserModel
        set(user) {
            val gson = Gson()
            val loginRes = gson.toJson(user)
            preferences.edit().putString("user", loginRes).apply()
        }

    var isLogin: Boolean
        get() = preferences.getBoolean("isLoginA", false)
        set(login) = preferences.edit().putBoolean("isLoginA", login).apply()

    var isFirstTime: Boolean
        get() = preferences.getBoolean("isFirstTime", false)
        set(login) = preferences.edit().putBoolean("isFirstTime", login).apply()


    fun clearPref() {
        preferences.edit().clear().apply()
    }

}