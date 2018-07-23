package tech.thdev.githubusersearch.data.source.search

import io.reactivex.Single
import tech.thdev.githubusersearch.data.GithubResponse
import tech.thdev.githubusersearch.network.GithubInterface

class GithubSearchRemoteDataSource(private val githubApi: GithubInterface) {

    fun searchUser(name: String, page: Int, perPage: Int): Single<GithubResponse> =
            githubApi.searchUser(name, page, perPage)
}