package tech.thdev.githubusersearch.domain

import tech.thdev.githubusersearch.domain.model.GitHubUserEntity

interface GitHubSearchRepository {

    suspend fun searchUser(name: String, perPage: Int): List<GitHubUserEntity>

    suspend fun getAllCacheItems(name: String): List<GitHubUserEntity>

    suspend fun searchUserLocal(name: String): List<GitHubUserEntity>

    suspend fun likeUserInfo(item: GitHubUserEntity)

    suspend fun getAllLocalData(): List<GitHubUserEntity>

    suspend fun unlikeUserInfo(item: GitHubUserEntity)

    fun clear()
}