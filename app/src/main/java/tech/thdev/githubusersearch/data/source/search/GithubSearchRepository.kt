package tech.thdev.githubusersearch.data.source.search

import io.reactivex.Single
import tech.thdev.githubusersearch.data.GithubResponse
import tech.thdev.githubusersearch.network.GithubInterface

class GithubSearchRepository private constructor(private val githubApi: GithubInterface) : GithubSearchDataSource {

    companion object {

        // For Singleton instantiation
        private var instance: GithubSearchRepository? = null

        fun getInstance(githubApi: GithubInterface) =
                instance ?: synchronized(this) {
                    instance ?: GithubSearchRepository(githubApi).also { instance = it }
                }
    }

    private val githubRemoteDataSource: GithubSearchDataSource by lazy {
        GithubSearchRemoteDataSource(githubApi)
    }

    override fun searchUser(name: String, page: Int, perPage: Int): Single<GithubResponse> =
            githubRemoteDataSource.searchUser(name, page, perPage)
}