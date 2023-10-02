@file:Suppress("NOTHING_TO_INLINE")

package tech.thdev.githubusersearch.util

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

private const val REQUEST_TIME_OUT = 60L

inline fun createRetrofit(
    json: Json,
    baseUrl: String,
): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(createOkHttpClient())
        .build()

fun createOkHttpClient(): OkHttpClient =
    OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor {
            println(it)
        }.setLevel(HttpLoggingInterceptor.Level.BODY))
        connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
    }.build()