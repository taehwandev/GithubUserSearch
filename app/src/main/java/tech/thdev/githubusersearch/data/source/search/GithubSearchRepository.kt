package tech.thdev.githubusersearch.data.source.search

import io.reactivex.Single
import tech.thdev.githubusersearch.data.GithubUser
import tech.thdev.githubusersearch.db.GithubRoomDatabase
import tech.thdev.githubusersearch.network.GithubInterface

class GithubSearchRepository private constructor(private val githubApi: GithubInterface,
                                                 private val githubRoomDatabase: GithubRoomDatabase) {

    companion object {

        // For Singleton instantiation
        private var instance: GithubSearchRepository? = null

        fun getInstance(githubApi: GithubInterface, githubRoomDatabase: GithubRoomDatabase) =
                instance ?: synchronized(this) {
                    instance
                            ?: GithubSearchRepository(githubApi, githubRoomDatabase).also { instance = it }
                }
    }

    private val searchCacheMap = mutableMapOf<String, MutableList<GithubUser>>()

    private val githubRemoteDataSource: GithubSearchRemoteDataSource by lazy {
        GithubSearchRemoteDataSource(githubApi)
    }

    private val githubLocalDataSource: GithubSearchLocalDataSource by lazy {
        GithubSearchLocalDataSource(githubRoomDatabase)
    }

    private val defaultPage = 1

    private var nowPage = defaultPage

    private fun getNowPage(name: String): Int {
        if (!searchCacheMap.containsKey(name)) {
            clear()
            nowPage = defaultPage
        } else {
            nowPage++
        }
        return nowPage
    }

    fun searchUser(name: String, perPage: Int): Single<MutableList<GithubUser>> =
            githubRemoteDataSource.searchUser(name, getNowPage(name), perPage)
                    .flatMap { githubResponse ->
                        searchUserLocal(name)
                                .map { localList ->
                                    githubResponse.items.also { itemList ->
                                        localList.forEach { localItem ->
                                            // 동기화
                                            itemList.filter {
                                                it.id == localItem.id
                                            }.forEach { it.isLike = true }
                                        }
                                    }
                                }
                    }
                    .map { githubUserList ->
                        // cache 꺼내와서 처리
                        searchCacheMap[name] = (searchCacheMap[name]
                                ?: mutableListOf()).also { it.addAll(githubUserList) }

                        searchCacheMap[name]?.toMutableList() ?: mutableListOf()
                    }

    fun getAllCacheItems(name: String): Single<MutableList<GithubUser>> {
        return Single.just(searchCacheMap[name] ?: mutableListOf())
                .flatMap { cacheList ->
                    searchUserLocal(name)
                            .map { localList ->
                                cacheList.forEach { cacheItem ->
                                    cacheItem.isLike = false

                                    localList.find { cacheItem.id == it.id }?.let {
                                        cacheItem.isLike = true
                                    }
                                }
                                cacheList
                            }
                }
    }

    fun clear() {
        searchCacheMap.clear()
    }

    fun searchUserLocal(name: String): Single<MutableList<GithubUser>> =
            githubLocalDataSource.searchUserLocal(name)

    fun likeUserInfo(item: GithubUser) {
        githubLocalDataSource.insertGithubUser(item)
    }

    fun getAllLocalData(): Single<MutableList<GithubUser>> {
        return githubLocalDataSource.getAllLikeUser()
    }

    fun unlikeUserInfo(it: GithubUser) {
        githubLocalDataSource.removeUser(it.login)
    }
}