package tech.thdev.githubusersearch.feature.main.holder.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.domain.model.GitHubSortType
import tech.thdev.githubusersearch.feature.main.holder.search.compose.SearchScreen
import tech.thdev.githubusersearch.feature.main.holder.search.compose.component.FabItem
import tech.thdev.githubusersearch.feature.main.holder.search.compose.component.MultiFloatingActionButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchComposableHolder(
    bottomBar: @Composable () -> Unit,
    searchViewModel: SearchViewModel,
) {
    val mainListUiState by searchViewModel.searchUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "GitHubUserSearch")
                },
            )
        },
        bottomBar = bottomBar,
        floatingActionButton = {
            MultiFloatingActionButton(
                fabIcon = painterResource(id = mainListUiState.sortIcon),
                items = persistentListOf(
                    FabItem(
                        icon = painterResource(id = R.drawable.ic_sort_numbers),
                        label = stringResource(id = R.string.label_sort_default),
                        onFabItemClicked = {
                            searchViewModel.changeSortType(GitHubSortType.FILTER_SORT_DEFAULT)
                        },
                    ),
                    FabItem(
                        icon = painterResource(id = R.drawable.ic_sort_alphabet),
                        label = stringResource(id = R.string.label_sort_name),
                        onFabItemClicked = {
                            searchViewModel.changeSortType(GitHubSortType.FILTER_SORT_NAME)
                        },
                    ),
                    FabItem(
                        icon = painterResource(id = R.drawable.ic_sort),
                        label = stringResource(id = R.string.label_sort_date_of_registration),
                        onFabItemClicked = {
                            searchViewModel.changeSortType(GitHubSortType.FILTER_SORT_DATE_OF_REGISTRATION)
                        },
                    ),
                ),
            )
        },
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            SearchScreen(searchViewModel = searchViewModel)
        }
    }
}