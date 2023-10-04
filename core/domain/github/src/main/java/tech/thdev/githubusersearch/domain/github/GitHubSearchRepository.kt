package tech.thdev.githubusersearch.domain.github

import kotlinx.coroutines.flow.Flow
import tech.thdev.githubusersearch.domain.github.model.GitHubSortType
import tech.thdev.githubusersearch.domain.github.model.GitHubUserEntity

interface GitHubSearchRepository {

    fun flowLoadData(searchKeyword: String, perPage: Int): Flow<List<GitHubUserEntity>>

    fun sortList(gitHubSortType: GitHubSortType)

    fun flowLoadLikedData(): Flow<List<GitHubUserEntity>>

    /**
     * 좋아요 상태 변경
     */
    suspend fun likeUserInfo(item: GitHubUserEntity)

    suspend fun unlikeUserInfo(id: Int)

    fun clear()
}