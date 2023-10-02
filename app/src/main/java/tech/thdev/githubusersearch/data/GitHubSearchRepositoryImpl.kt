package tech.thdev.githubusersearch.data

import androidx.annotation.VisibleForTesting
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.PublishSubject
import tech.thdev.githubusersearch.data.model.GitHubUserInfoResponse
import tech.thdev.githubusersearch.database.GitHubUserDao
import tech.thdev.githubusersearch.database.model.GitHubUser
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.model.GitHubSortType
import tech.thdev.githubusersearch.domain.model.GitHubUserEntity

class GitHubSearchRepositoryImpl internal constructor(
    private val gitHubApi: GitHubApi,
    private val gitHubUserDao: GitHubUserDao,
) : GitHubSearchRepository {

    @VisibleForTesting
    val newList = PublishSubject.create<List<GitHubUserInfoResponse>>()

    @VisibleForTesting
    var sortList: GitHubSortType = GitHubSortType.FILTER_SORT_DEFAULT

    @VisibleForTesting
    val cacheList = mutableListOf<GitHubUserInfoResponse>()

    @VisibleForTesting
    var cacheSearchKeyword = ""

    @VisibleForTesting
    var page = 1

    @VisibleForTesting
    var endPage = false

    override fun observeData(): Flowable<List<GitHubUserEntity>> =
        Flowable
            .combineLatest(
                newList.toFlowable(BackpressureStrategy.LATEST),
                gitHubUserDao.allLiked()
            ) { items, likedList ->
                items to likedList
            }
            .map { (items, likedList) ->
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
            .map {
                it.sort(sortList)
            }

    override fun loadData(searchKeyword: String, perPage: Int): Flowable<Boolean> =
        Flowable.just(true)
            .doOnEach {
                if (cacheSearchKeyword == searchKeyword) {
                    page++
                } else {
                    clear()
                }
            }
            .switchMapSingle {
                gitHubApi.searchUser(
                    searchKeyword = searchKeyword,
                    page = page,
                    perPage = perPage,
                )
            }
            .doOnNext {
                cacheSearchKeyword = searchKeyword
                endPage = it.incompleteResults
                cacheList.addAll(it.items)
            }
            .doOnNext {
                newList.onNext(cacheList)
            }
            .map { true }

    override fun sortList(gitHubSortType: GitHubSortType) {
        sortList = gitHubSortType
        newList.onNext(cacheList)
    }

    private fun List<GitHubUserEntity>.sort(gitHubSortType: GitHubSortType): List<GitHubUserEntity> =
        when (gitHubSortType) {
            GitHubSortType.FILTER_SORT_DEFAULT -> this
            GitHubSortType.FILTER_SORT_NAME -> this.sortedBy { it.login }
            GitHubSortType.FILTER_SORT_DATE_OF_REGISTRATION -> this.sortedBy { it.id }
        }

    override fun observeLoadLikedData(): Flowable<List<GitHubUserEntity>> =
        gitHubUserDao.allLiked()
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

    override fun likeUserInfo(item: GitHubUserEntity) {
        gitHubUserDao.insert(
            GitHubUser(
                id = item.id,
                login = item.login,
                avatarUrl = item.avatarUrl,
                score = item.score,
            )
        )
    }

    override fun unlikeUserInfo(id: Int) {
        gitHubUserDao.deleteUser(id)
    }

    override fun clear() {
        cacheList.clear()
        endPage = false
        page = 1
        cacheSearchKeyword = ""
    }

    companion object {

        // For Singleton instantiation
        private var instance: GitHubSearchRepositoryImpl? = null

        fun getInstance(gitHubApi: GitHubApi, gitHubUserDao: GitHubUserDao) =
            instance ?: synchronized(this) {
                instance ?: GitHubSearchRepositoryImpl(gitHubApi, gitHubUserDao).also {
                    instance = it
                }
            }
    }
}