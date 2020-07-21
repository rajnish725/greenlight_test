package com.fycus.api


import com.fycus.models.CommonDataResponse
import com.fycus.models.UserModel
import com.fycus.models.response.FaqMainModel
import com.fycus.models.response.HelpModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable
import javax.inject.Singleton

@Singleton
interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun callSignUpAPI(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("c_password") c_password: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String
    ): Observable<CommonDataResponse<UserModel>>

    @FormUrlEncoded
    @POST("login")
    fun callLoginAPI(
        @Field("loginid") user_id: String,
        @Field("password") password: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String,
        @Field("type") type: String
    ): Observable<CommonDataResponse<UserModel>>

    @GET("get_details")
    fun callGetProfileAPI(@Header("Authorization") token: String): Observable<CommonDataResponse<UserModel>>

    @GET("get_help")
    fun callGetHelpAPI(): Observable<CommonDataResponse<HelpModel>>

    @GET("get_faq")
    fun callGetFaqAPI(): Observable<CommonDataResponse<List<FaqMainModel>>>

    @Multipart
    @POST("update_details")
    fun callUpdateProfileAPI(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("email") email: RequestBody
    ): Observable<CommonDataResponse<UserModel>>


    @FormUrlEncoded
    @POST("send_otp")
    fun callSendForgotOTPAPI(
        @Field("user_id") user_id: String,
        @Field("otp") otp: String,
        @Field("type") type: String
    ): Observable<CommonDataResponse<UserModel>>

    @FormUrlEncoded
    @POST("change_password")
    fun callPasswordUpdateAPI(
        @Field("user_id") user_id: String,
        @Field("new_password") new_password: String
    ): Observable<CommonDataResponse<UserModel>>

}