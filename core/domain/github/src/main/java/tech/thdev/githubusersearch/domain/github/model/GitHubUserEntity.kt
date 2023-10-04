package tech.thdev.githubusersearch.domain.github.model

data class GitHubUserEntity(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val score: Double,
    val isLike: Boolean,
) {

    companion object {

        val Default = GitHubUserEntity(
            login = "",
            id = 0,
            avatarUrl = "",
            score = 0.0,
            isLike = false,
        )
    }
}