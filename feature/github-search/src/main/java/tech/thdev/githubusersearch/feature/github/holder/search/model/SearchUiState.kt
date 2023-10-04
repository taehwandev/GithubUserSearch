package tech.thdev.githubusersearch.feature.github.holder.search.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import tech.thdev.githubusersearch.design.system.R
import tech.thdev.githubusersearch.domain.github.model.GitHubSortType

@Immutable
data class SearchUiState(
    val searchKeyword: String,
    val uiState: UiState,
    val sortType: GitHubSortType,
    val sortIcon: Int,
) {

    @Stable
    sealed interface UiState {

        @Immutable
        data object Loading : UiState

        @Immutable
        data class Empty(val message: String?) : UiState {

            companion object {

                val Default = Empty(
                    message = null,
                )
            }
        }

        @Immutable
        data class UserItems(
            val items: PersistentList<Info>,
        ) : UiState {

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

    companion object {

        val Default = SearchUiState(
            searchKeyword = "",
            uiState = UiState.Empty.Default,
            sortType = GitHubSortType.FILTER_SORT_DEFAULT,
            sortIcon = R.drawable.ic_sort_numbers,
        )
    }
}