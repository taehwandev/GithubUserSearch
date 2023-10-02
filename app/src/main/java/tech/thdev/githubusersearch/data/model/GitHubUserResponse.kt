package tech.thdev.githubusersearch.data.model

import com.google.gson.annotations.SerializedName

data class GitHubUserResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: MutableList<GitHubUserInfoResponse>,
)

data class GitHubUserInfoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("score") val score: Double,
)