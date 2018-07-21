@file:Suppress("NOTHING_TO_INLINE")

package tech.thdev.githubusersearch.util

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tech.thdev.githubusersearch.BuildConfig
import java.util.concurrent.TimeUnit

const val REQUEST_TIME_OUT = 60L

inline fun <T> createRetrofit(cls: Class<T>, baseUrl: String, noinline isInternetAvailable: () -> Boolean): T =
        Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient(isInternetAvailable))
                .build()
                .create(cls)

fun createOkHttpClient(isInternetAvailable: () -> Boolean): OkHttpClient =
        OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                // println log tag name is "API"
                println(it)
            }).setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
            connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
            readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
            addInterceptor(networkCheckInterceptor(isInternetAvailable))
        }.build()

fun networkCheckInterceptor(isInternetAvailable: () -> Boolean) = Interceptor { chain ->
    chain.run {
//        println(this.proceed(request()).header("Link"))
        // next...?
        if (!isInternetAvailable()) {
            throw NoNetworkException()
        }
        proceed(chain.request())
    }
}

class NoNetworkException : Throwable()