package tech.thdev.githubusersearch.feature.main.holder.search.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.feature.main.compose.component.ResultEmpty
import tech.thdev.githubusersearch.feature.main.holder.search.SearchViewModel
import tech.thdev.githubusersearch.feature.main.holder.search.compose.component.SearchResult
import tech.thdev.githubusersearch.feature.main.holder.search.compose.component.SearchResultLoading
import tech.thdev.githubusersearch.feature.main.holder.search.compose.component.SearchTextField
import tech.thdev.githubusersearch.feature.main.holder.search.model.SearchUiState

@Composable
internal fun SearchScreen(
    searchViewModel: SearchViewModel,
) {
    val mainListUiState by searchViewModel.searchUiState.collectAsStateWithLifecycle()

    SearchScreen(
        mainListUiState = mainListUiState,
        onClick = searchViewModel::selectedLikeChange,
        onValueChange = searchViewModel::changeSearchKeyword,
        onSearchClick = searchViewModel::searchKeyword,
    )
}

@Composable
private fun SearchScreen(
    mainListUiState: SearchUiState,
    onClick: (SearchUiState.UiState.UserItems.Info) -> Unit = {},
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SearchTextField(
            value = mainListUiState.searchKeyword,
            hint = stringResource(id = R.string.search_hint),
            onValueChange = onValueChange,
            onClick = onSearchClick,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClick()
                }
            )
        )

        when (mainListUiState.uiState) {
            is SearchUiState.UiState.Loading -> {
                SearchResultLoading(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            is SearchUiState.UiState.Empty -> {
                ResultEmpty(
                    message = mainListUiState.uiState.message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            is SearchUiState.UiState.UserItems -> {
                SearchResult(
                    userItems = mainListUiState.uiState.items,
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
        mainListUiState = SearchUiState.Default.copy(
            uiState = SearchUiState.UiState.Empty(
                message = "Empty result",
            ),
        ),
        onClick = {},
        onValueChange = {},
        onSearchClick = {},
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
internal fun PreviewSearchScreenItems() {
    SearchScreen(
        mainListUiState = SearchUiState.Default.copy(
            uiState = SearchUiState.UiState.UserItems(
                items = persistentListOf(
                    SearchUiState.UiState.UserItems.Info.Default.copy(
                        login = "a",
                        score = 0.14,
                    ),
                    SearchUiState.UiState.UserItems.Info.Default.copy(
                        login = "b",
                        score = 1.0,
                        isLike = true,
                    ),
                ),
            ),
        ),
        onClick = {},
        onValueChange = {},
        onSearchClick = {},
    )
}