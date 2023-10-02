package tech.thdev.githubusersearch.network

import kotlinx.serialization.json.Json
import tech.thdev.githubusersearch.data.GitHubApi
import tech.thdev.githubusersearch.util.createRetrofit

object RetrofitFactory {

    val gitHubApi: GitHubApi by lazy {
        createRetrofit(
            json = Json {
                coerceInputValues = true
                ignoreUnknownKeys = true
            },
            baseUrl = BASE_URL,
        ).create(GitHubApi::class.java)
    }

    private const val BASE_URL = "https://api.github.com"
}