package tech.thdev.githubusersearch.feature.main.search.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import tech.thdev.githubusersearch.feature.main.LikeChangeViewModel
import tech.thdev.githubusersearch.feature.main.model.MainListUiState
import tech.thdev.githubusersearch.feature.main.search.SearchViewModel
import tech.thdev.githubusersearch.feature.main.search.compose.component.SearchResult
import tech.thdev.githubusersearch.feature.main.search.compose.component.SearchResultEmpty
import tech.thdev.githubusersearch.feature.main.search.compose.component.SearchResultLoading

@Composable
internal fun SearchScreen(
    searchViewModel: SearchViewModel,
    likeChangeViewModel: LikeChangeViewModel,
) {
    val mainListUiState by searchViewModel.mainListUiState.collectAsStateWithLifecycle()

    SearchScreen(
        mainListUiState = mainListUiState,
        onClick = { item ->
            likeChangeViewModel.selectedLikeChange(item)
        }
    )
}

@Composable
private fun SearchScreen(
    mainListUiState: MainListUiState,
    onClick: (MainListUiState.UserItems.Info) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        when (mainListUiState) {
            is MainListUiState.Loading -> {
                SearchResultLoading(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            is MainListUiState.Empty -> {
                SearchResultEmpty(
                    message = mainListUiState.message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            is MainListUiState.UserItems -> {
                SearchResult(
                    userItems = mainListUiState.items,
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
internal fun PreviewSearchScreenEmpty() {
    SearchScreen(
        mainListUiState = MainListUiState.Empty(
            message = "Empty result",
        ),
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
internal fun PreviewSearchScreenItems() {
    SearchScreen(
        mainListUiState = MainListUiState.UserItems(
            items = persistentListOf(
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
        ),
    )
}