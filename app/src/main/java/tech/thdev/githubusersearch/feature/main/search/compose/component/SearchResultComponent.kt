package tech.thdev.githubusersearch.feature.main.search.compose.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import tech.thdev.githubusersearch.feature.main.compose.component.SearchResultItem
import tech.thdev.githubusersearch.feature.main.model.MainListUiState

@Composable
internal fun SearchResult(
    modifier: Modifier = Modifier,
    userItems: PersistentList<MainListUiState.UserItems.Info>,
    onClick: (MainListUiState.UserItems.Info) -> Unit = {},
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
            SearchResultItem(
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
internal fun PreviewSearchResult() {
    SearchResult(
        userItems = persistentListOf(
            MainListUiState.UserItems.Info.Default.copy(
                login = "a",
                score = 0.14,
            ),
            MainListUiState.UserItems.Info.Default.copy(
                login = "b",
                score = 1.0,
                isLike = true,
            ),
        ),
    )
}