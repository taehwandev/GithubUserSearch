package tech.thdev.githubusersearch.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubUserResponse(
    @SerialName("total_count") val totalCount: Int,
    @SerialName("incomplete_results") val incompleteResults: Boolean,
    @SerialName("items") val items: MutableList<GitHubUserInfoResponse>,
)

@Serializable
data class GitHubUserInfoResponse(
    @SerialName("login") val login: String,
    @SerialName("id") val id: Int,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("score") val score: Double,
)