package tech.thdev.githubusersearch.data

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import tech.thdev.githubusersearch.data.model.GitHubUserInfoResponse
import tech.thdev.githubusersearch.database.GitHubUserDao
import tech.thdev.githubusersearch.database.model.GitHubUser
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.model.GitHubSortType
import tech.thdev.githubusersearch.domain.model.GitHubUserEntity

class GitHubSearchRepositoryImpl private constructor(
    private val gitHubApi: GitHubApi,
    private val gitHubUserDao: GitHubUserDao,
) : GitHubSearchRepository {

    private val sortList = MutableStateFlow(GitHubSortType.FILTER_SORT_DEFAULT)

    @VisibleForTesting
    val cacheList = mutableListOf<GitHubUserInfoResponse>()

    @VisibleForTesting
    var cacheSearchKeyword = ""

    @VisibleForTesting
    var page = 1

    @VisibleForTesting
    var endPage = false

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun flowLoadData(searchKeyword: String, perPage: Int): Flow<List<GitHubUserEntity>> =
        flow {
            if (cacheSearchKeyword == searchKeyword) {
                page++
            } else {
                cacheList.clear()
                page = 1
                endPage = false
            }
            emit(true)
        }
            .map {
                gitHubApi.searchUser(
                    searchKeyword = searchKeyword,
                    page = page,
                    perPage = perPage,
                )
            }
            .onEach {
                cacheSearchKeyword = searchKeyword
                endPage = it.incompleteResults
            }
            .onEach {
                cacheList.addAll(it.items)
            }
            .map { cacheList }
            .flatMapLatest { items ->
                gitHubUserDao.flowLiked()
                    .map {
                        it to items
                    }
            }
            .flatMapLatest { data ->
                sortList
                    .map { data }
            }
            .map { (likedList, items) ->
                items.map { item ->
                    GitHubUserEntity(
                        id = item.id,
                        login = item.login,
                        avatarUrl = item.avatarUrl,
                        score = item.score,
                        isLike = likedList.firstOrNull { likedItem -> likedItem.id == item.id } != null,
                    )
                }
            }
            .map { newList ->
                newList.sort(sortList.value)
            }

    override fun flowLoadLikedData(): Flow<List<GitHubUserEntity>> =
        gitHubUserDao
            .flowLiked()
            .map {
                it.map { item ->
                    GitHubUserEntity(
                        id = item.id,
                        login = item.login,
                        avatarUrl = item.avatarUrl,
                        score = item.score,
                        isLike = true,
                    )
                }
            }

    private fun List<GitHubUserEntity>.sort(gitHubSortType: GitHubSortType): List<GitHubUserEntity> =
        when (gitHubSortType) {
            GitHubSortType.FILTER_SORT_DEFAULT -> this
            GitHubSortType.FILTER_SORT_NAME -> this.sortedBy { it.login }
            GitHubSortType.FILTER_SORT_DATE_OF_REGISTRATION -> this.sortedBy { it.id }
        }

    override fun sortList(gitHubSortType: GitHubSortType) {
        sortList.value = gitHubSortType
    }

    override suspend fun likeUserInfo(item: GitHubUserEntity) {
        gitHubUserDao.insert(
            user = GitHubUser(
                id = item.id,
                login = item.login,
                avatarUrl = item.avatarUrl,
                score = item.score,
            )
        )
    }

    override suspend fun unlikeUserInfo(id: Int) {
        gitHubUserDao.deleteUser(id)
    }

    override fun clear() {
        cacheList.clear()
        endPage = false
        page = 1
        cacheSearchKeyword = ""
    }

    companion object {

        private var instance: GitHubSearchRepositoryImpl? = null

        fun getInstance(
            gitHubApi: GitHubApi,
            gitHubUserDao: GitHubUserDao,
        ): GitHubSearchRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: GitHubSearchRepositoryImpl(
                    gitHubApi = gitHubApi,
                    gitHubUserDao = gitHubUserDao,
                ).also { instance = it }
            }
    }
}