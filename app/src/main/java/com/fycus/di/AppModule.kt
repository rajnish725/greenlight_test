package com.fycus.di

import android.app.Application
import android.content.Context
import com.fycus.api.ApiService
import com.fycus.prefers.UserPref
import com.fycus.utils.Utils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.supernebr.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
internal class AppModule {

    @Provides
    fun providesContext(app: Application): Context {
        return app.applicationContext
    }

    @Provides
    fun providesGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .connectTimeout((60 * 5).toLong(), TimeUnit.SECONDS)
            .readTimeout((60 * 5).toLong(), TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)

        builder.addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
            requestBuilder.header("Content-Type", "application/json")
            requestBuilder.method(original.method(), original.body())

            val request = requestBuilder.build()

            chain.proceed(request)
        }

        return builder.build()
    }


    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())



        return builder.build()
    }

    @Provides
    fun providesApiServices(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun providesUtils(context: Context): Utils {
        return Utils(context)
    }

    @Provides
    fun provideUserPref(context: Context): UserPref {
        return UserPref(context)
    }
}