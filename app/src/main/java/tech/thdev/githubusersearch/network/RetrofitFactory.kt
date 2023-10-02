package tech.thdev.githubusersearch.network

import tech.thdev.githubusersearch.data.GitHubApi
import tech.thdev.githubusersearch.util.createRetrofit

object RetrofitFactory {

    val gitHubApi: GitHubApi by lazy {
        createRetrofit(BASE_URL)
            .create(GitHubApi::class.java)
    }

    private const val BASE_URL = "https://api.github.com"
}