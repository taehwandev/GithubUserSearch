package tech.thdev.githubusersearch.feature.github.holder.like.compose.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import tech.thdev.githubusersearch.feature.github.compose.component.ResultItem
import tech.thdev.githubusersearch.feature.github.holder.like.model.LikeUiState

@Composable
internal fun LikeResult(
    modifier: Modifier = Modifier,
    userItems: PersistentList<LikeUiState.UserItems.Info>,
    onClick: (LikeUiState.UserItems.Info) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = userItems,
            key = {
                it.id + it.hashCode()
            },
        ) { item ->
            ResultItem(
                login = item.login,
                avatarUrl = item.avatarUrl,
                score = item.score,
                isLike = item.isLike,
                onClick = {
                    onClick(item)
                },
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
internal fun PreviewLikeResult() {
    LikeResult(
        userItems = persistentListOf(
            LikeUiState.UserItems.Info.Default.copy(
                login = "a",
                score = 0.14,
            ),
            LikeUiState.UserItems.Info.Default.copy(
                login = "b",
                score = 1.0,
                isLike = true,
            ),
        ),
    )
}