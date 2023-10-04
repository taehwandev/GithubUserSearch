package tech.thdev.githubusersearch.data.github.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubUserResponse(
    @SerialName("total_count") val totalCount: Int = 0,
    @SerialName("incomplete_results") val incompleteResults: Boolean = false,
    @SerialName("items") val items: List<GitHubUserInfoResponse> = emptyList(),
)

@Serializable
data class GitHubUserInfoResponse(
    @SerialName("id") val id: Int = 0,
    @SerialName("login") val login: String = "",
    @SerialName("avatar_url") val avatarUrl: String = "",
    @SerialName("score") val score: Double = 0.0,
)