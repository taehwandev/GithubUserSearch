package tech.thdev.githubusersearch.data

import com.google.gson.annotations.SerializedName

data class GitHubResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: MutableList<GitHubUser>,
)