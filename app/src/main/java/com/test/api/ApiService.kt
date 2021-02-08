package com.test.api


import com.test.models.CommonDataResponse
import com.test.models.MetricsModel
import retrofit2.http.*
import rx.Observable
import javax.inject.Singleton

@Singleton
interface ApiService {
    @GET("assignment")
    fun getMetricsData(): Observable<CommonDataResponse<MetricsModel>>

}
