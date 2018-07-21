package tech.thdev.githubusersearch.data.source.search

import io.reactivex.Single
import tech.thdev.githubusersearch.data.GithubResponse

interface GithubSearchDataSource {

    fun searchUser(name: String, page: Int, perPage: Int): Single<GithubResponse>
}