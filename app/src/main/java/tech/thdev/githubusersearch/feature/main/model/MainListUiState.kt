package tech.thdev.githubusersearch.feature.main.model

sealed interface MainListUiState {

    data object NetworkFail : MainListUiState

    data class Error(val message: String?) : MainListUiState

    data class UserItems(
        val items: List<Info>,
    ) : MainListUiState {

        data class Info(
            val login: String,
            val id: Int,
            val avatarUrl: String,
            val score: Double,
            val isLike: Boolean,
        )

        companion object {

            val Default = UserItems(
                items = emptyList(),
            )
        }
    }
}