package tech.thdev.githubusersearch.data.source.search

import io.reactivex.rxjava3.core.Single
import tech.thdev.githubusersearch.data.GitHubResponse
import tech.thdev.githubusersearch.network.GitHubInterface

class GitHubSearchRemoteDataSource(private val githubApi: GitHubInterface) {

    fun searchUser(name: String, page: Int, perPage: Int): Single<GitHubResponse> =
        githubApi.searchUser(name, page, perPage)
}