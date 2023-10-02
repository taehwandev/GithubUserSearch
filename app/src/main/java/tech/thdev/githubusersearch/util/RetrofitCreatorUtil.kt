@file:Suppress("NOTHING_TO_INLINE")

package tech.thdev.githubusersearch.util

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val REQUEST_TIME_OUT = 60L

inline fun createRetrofit(
    baseUrl: String,
): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(createOkHttpClient())
        .build()

fun createOkHttpClient(): OkHttpClient =
    OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor {
            // println log tag name is "API"
            println(it)
        }.setLevel(HttpLoggingInterceptor.Level.BODY))
        connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
    }.build()