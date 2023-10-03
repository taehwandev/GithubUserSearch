package tech.thdev.githubusersearch.feature.main.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed interface MainListUiState {

    @Immutable
    data object Loading : MainListUiState

    @Immutable
    data class Empty(val message: String?) : MainListUiState {

        companion object {

            val Default = Empty(
                message = null,
            )
        }
    }

    @Immutable
    data class UserItems(
        val items: PersistentList<Info>,
    ) : MainListUiState {

        @Immutable
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
                items = persistentListOf(),
            )
        }
    }
}