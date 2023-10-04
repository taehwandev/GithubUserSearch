package tech.thdev.githubusersearch.feature.github.holder.search.model.convert

import kotlinx.collections.immutable.toPersistentList
import tech.thdev.githubusersearch.domain.github.model.GitHubUserEntity
import tech.thdev.githubusersearch.feature.github.holder.search.model.SearchUiState

internal fun List<GitHubUserEntity>.convert(
    prevItem: SearchUiState,
): SearchUiState =
    prevItem.copy(
        uiState = SearchUiState.UiState.UserItems(
            items = map { item ->
                SearchUiState.UiState.UserItems.Info(
                    id = item.id,
                    login = item.login,
                    avatarUrl = item.avatarUrl,
                    score = item.score,
                    isLike = item.isLike,
                )
            }.toSet().toPersistentList()
        )
    )