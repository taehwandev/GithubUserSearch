package tech.thdev.githubusersearch.network

import kotlinx.serialization.json.Json
import tech.thdev.githubusersearch.GithubApp
import tech.thdev.githubusersearch.data.GitHubApi
import tech.thdev.githubusersearch.util.createRetrofit
import tech.thdev.githubusersearch.util.isOnline

object RetrofitFactory {

    const val baseUrl = "https://api.github.com"

    val githubApi: GitHubApi by lazy {
        createRetrofit(
            json = Json {
                coerceInputValues = true
                ignoreUnknownKeys = true
            },
            baseUrl = baseUrl,
        ) {
            GithubApp.context.isOnline()
        }.create(GitHubApi::class.java)
    }
}