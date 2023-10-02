package tech.thdev.githubusersearch.feature.main.model.convert

import tech.thdev.githubusersearch.domain.model.GitHubUserEntity
import tech.thdev.githubusersearch.feature.main.model.MainListUiState

internal fun List<GitHubUserEntity>.convert(): MainListUiState =
    MainListUiState.UserItems(
        items = map { item ->
            MainListUiState.UserItems.Info(
                id = item.id,
                login = item.login,
                avatarUrl = item.avatarUrl,
                score = item.score,
                isLike = item.isLike,
            )
        }
    )