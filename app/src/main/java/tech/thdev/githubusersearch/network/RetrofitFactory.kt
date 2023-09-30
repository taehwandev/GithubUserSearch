package tech.thdev.githubusersearch.network

import tech.thdev.githubusersearch.GithubApp
import tech.thdev.githubusersearch.util.createRetrofit
import tech.thdev.githubusersearch.util.isOnline

object RetrofitFactory {

    const val baseUrl = "https://api.github.com"

    val githubApi: GitHubInterface by lazy {
        createRetrofit(GitHubInterface::class.java, baseUrl) {
            GithubApp.context.isOnline()
        }
    }
}