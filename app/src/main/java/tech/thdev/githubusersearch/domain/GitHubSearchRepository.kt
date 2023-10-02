package tech.thdev.githubusersearch.domain

import io.reactivex.rxjava3.core.Flowable
import tech.thdev.githubusersearch.domain.model.GitHubSortType
import tech.thdev.githubusersearch.domain.model.GitHubUserEntity

interface GitHubSearchRepository {

    fun observeData(): Flowable<List<GitHubUserEntity>>

    fun loadData(searchKeyword: String, perPage: Int): Flowable<Boolean>

    fun sortList(gitHubSortType: GitHubSortType)

    fun observeLoadLikedData(): Flowable<List<GitHubUserEntity>>

    /**
     * 좋아요 상태 변경
     */
    fun likeUserInfo(item: GitHubUserEntity)

    fun unlikeUserInfo(id: Int)

    fun clear()
}