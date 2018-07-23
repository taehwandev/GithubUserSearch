package tech.thdev.githubusersearch.data.source.search

import android.util.Log
import io.reactivex.Single
import tech.thdev.githubusersearch.data.GithubResponse
import tech.thdev.githubusersearch.data.GithubUser
import tech.thdev.githubusersearch.network.GithubInterface

class GithubSearchRepository private constructor(private val githubApi: GithubInterface) {

    companion object {

        // For Singleton instantiation
        private var instance: GithubSearchRepository? = null

        fun getInstance(githubApi: GithubInterface) =
                instance ?: synchronized(this) {
                    instance ?: GithubSearchRepository(githubApi).also { instance = it }
                }
    }

    private val searchCacheMap = mutableMapOf<String, MutableList<GithubUser>>()

    private val githubRemoteDataSource: GithubSearchRemoteDataSource by lazy {
        GithubSearchRemoteDataSource(githubApi)
    }

    fun searchUser(name: String, page: Int, perPage: Int): Single<GithubResponse> =
            githubRemoteDataSource.searchUser(name, page, perPage)
                    .map {
                        it.also { item ->
                            if (!searchCacheMap.containsKey(name)) {
                                clear()
                            }
                            // cache 꺼내와서 처리
                            searchCacheMap[name] = (searchCacheMap[name]
                                    ?: mutableListOf()).also { it.addAll(item.items) }

                            Log.d("GithubSearchRepository", "query : $name - ${searchCacheMap[name]}")
                        }
                    }

    fun getAllCacheItems(name: String): Single<MutableList<GithubUser>> {
        Log.d("GithubSearchRepository", "query $name")
        return Single.just(searchCacheMap[name]!!.toMutableList())
    }

    fun clear() {
        searchCacheMap.clear()
    }
}