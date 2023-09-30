package tech.thdev.githubusersearch.domain.model

data class GitHubUserEntity(
    val login: String,
    val id: Int,
    val avatarUrl: String,
    val score: Double,
    val isLike: Boolean,
)