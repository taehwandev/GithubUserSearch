package tech.thdev.githubusersearch.feature.github.holder.like.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed interface LikeUiState {

    @Immutable
    data class Empty(val message: String?) : LikeUiState {

        companion object {

            val Default = Empty(
                message = null,
            )
        }
    }

    @Immutable
    data class UserItems(
        val items: PersistentList<Info>,
    ) : LikeUiState {

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