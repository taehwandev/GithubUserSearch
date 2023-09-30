package tech.thdev.githubusersearch.data

import tech.thdev.githubusersearch.database.GithubUserDao
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.model.GitHubUserEntity

class GitHubSearchRepositoryImpl(
    private val githubApi: GitHubApi,
    private val githubUserDao: GithubUserDao,
): GitHubSearchRepository {
    override suspend fun searchUser(name: String, perPage: Int): List<GitHubUserEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCacheItems(name: String): List<GitHubUserEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun searchUserLocal(name: String): List<GitHubUserEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun likeUserInfo(item: GitHubUserEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLocalData(): List<GitHubUserEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun unlikeUserInfo(item: GitHubUserEntity) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        // remove cache
    }

    //
//    companion object {
//
//        // For Singleton instantiation
//        private var instance: GitHubSearchRepositoryImpl? = null
//
//        fun getInstance(githubApi: GitHubInterface, githubRoomDatabase: GitHubRoomDatabase) =
//            instance ?: synchronized(this) {
//                instance
//                    ?: GitHubSearchRepositoryImpl(githubApi, githubRoomDatabase).also { instance = it }
//            }
//    }
//
//    private val searchCacheMap = mutableMapOf<String, MutableList<GitHubUser>>()
//
//    private val githubRemoteDataSource: GitHubSearchRemoteDataSource by lazy {
//        GitHubSearchRemoteDataSource(githubApi)
//    }
//
//    private val githubLocalDataSource: GitHubSearchLocalDataSource by lazy {
//        GitHubSearchLocalDataSource(githubRoomDatabase)
//    }
//
//    private val defaultPage = 1
//
//    private var nowPage = defaultPage
//
//    private fun getNowPage(name: String): Int {
//        if (!searchCacheMap.containsKey(name)) {
//            clear()
//            nowPage = defaultPage
//        } else {
//            nowPage++
//        }
//        return nowPage
//    }
//
//    fun searchUser(name: String, perPage: Int): Single<MutableList<GitHubUser>> =
//        githubRemoteDataSource.searchUser(name, getNowPage(name), perPage)
//            .flatMap { githubResponse ->
//                searchUserLocal(name)
//                    .map { localList ->
//                        githubResponse.items.also { itemList ->
//                            localList.forEach { localItem ->
//                                // 동기화
//                                itemList.filter {
//                                    it.id == localItem.id
//                                }.forEach { it.isLike = true }
//                            }
//                        }
//                    }
//            }
//            .map { githubUserList ->
//                // cache 꺼내와서 처리
//                searchCacheMap[name] = (searchCacheMap[name]
//                    ?: mutableListOf()).also { it.addAll(githubUserList) }
//
//                searchCacheMap[name]?.toMutableList() ?: mutableListOf()
//            }
//
//    fun getAllCacheItems(name: String): Single<MutableList<GitHubUser>> {
//        return Single.just(searchCacheMap[name] ?: mutableListOf())
//            .flatMap { cacheList ->
//                searchUserLocal(name)
//                    .map { localList ->
//                        cacheList.forEach { cacheItem ->
//                            cacheItem.isLike = false
//
//                            localList.find { cacheItem.id == it.id }?.let {
//                                cacheItem.isLike = true
//                            }
//                        }
//                        cacheList
//                    }
//            }
//    }
//
//    fun clear() {
//        searchCacheMap.clear()
//    }
//
//    fun searchUserLocal(name: String): Single<MutableList<GitHubUser>> =
//        githubLocalDataSource.searchUserLocal(name)
//
//    fun likeUserInfo(item: GitHubUser) {
//        githubLocalDataSource.insertGithubUser(item)
//    }
//
//    fun getAllLocalData(): Single<MutableList<GitHubUser>> {
//        return githubLocalDataSource.getAllLikeUser()
//    }
//
//    fun unlikeUserInfo(it: GitHubUser) {
//        githubLocalDataSource.removeUser(it.login)
//    }
}