@file:Suppress("NOTHING_TO_INLINE")

package tech.thdev.githubusersearch.util

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val REQUEST_TIME_OUT = 60L

inline fun createRetrofit(
    json: Json,
    baseUrl: String,
    noinline isInternetAvailable: () -> Boolean,
): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(createOkHttpClient(isInternetAvailable))
        .build()

fun createOkHttpClient(
    isInternetAvailable: () -> Boolean,
): OkHttpClient =
    OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor {
            println(it)
        }.setLevel(HttpLoggingInterceptor.Level.BODY))
        connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        addInterceptor(networkCheckInterceptor(isInternetAvailable))
    }.build()

fun networkCheckInterceptor(
    isInternetAvailable: () -> Boolean,
) = Interceptor { chain ->
    chain.run {
        if (isInternetAvailable().not()) {
            throw NoNetworkException("Is not available network!!!")
        }
        proceed(chain.request())
    }
}

class NoNetworkException(message: String) : Throwable(message)