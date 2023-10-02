package tech.thdev.githubusersearch.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import tech.thdev.githubusersearch.data.GitHubApi

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val REQUEST_TIME_OUT = 60L

    private const val BASE_URL = "https://api.github.com"

    @Provides
    fun provideJson(): Json =
        Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor {
                println(it)
            }.setLevel(HttpLoggingInterceptor.Level.BODY))
            connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
            readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        }.build()

    @Provides
    fun provideRetrofit(
        json: Json,
        okHttpClient: OkHttpClient,
    ): GitHubApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
            .create(GitHubApi::class.java)
}