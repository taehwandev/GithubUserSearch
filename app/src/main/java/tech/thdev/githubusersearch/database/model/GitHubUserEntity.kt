package tech.thdev.githubusersearch.database.model

data class GitHubUserEntity(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val score: Double,
)