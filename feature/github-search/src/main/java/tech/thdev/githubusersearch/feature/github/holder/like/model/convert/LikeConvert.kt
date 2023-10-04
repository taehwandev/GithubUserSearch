package tech.thdev.githubusersearch.feature.github.holder.like.model.convert

import kotlinx.collections.immutable.toPersistentList
import tech.thdev.githubusersearch.domain.github.model.GitHubUserEntity
import tech.thdev.githubusersearch.feature.github.holder.like.model.LikeUiState

internal fun List<GitHubUserEntity>.convert(): LikeUiState =
    LikeUiState.UserItems(
        items = map { item ->
            LikeUiState.UserItems.Info(
                id = item.id,
                login = item.login,
                avatarUrl = item.avatarUrl,
                score = item.score,
                isLike = item.isLike,
            )
        }.toSet().toPersistentList()
    )