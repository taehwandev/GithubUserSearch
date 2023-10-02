package tech.thdev.githubusersearch.feature.main.model

sealed interface MainListUiState {

    data class Error(val message: String?) : MainListUiState

    data class UserItems(
        val items: List<Info>,
    ) : MainListUiState {

        data class Info(
            val id: Int,
            val login: String,
            val avatarUrl: String,
            val score: Double,
            val isLike: Boolean,
        ) {

            companion object {

                val Default = Info(
                    id = 0,
                    login = "",
                    avatarUrl = "",
                    score = 0.0,
                    isLike = false,
                )
            }
        }

        companion object {

            val Default = UserItems(
                items = emptyList(),
            )
        }
    }
}