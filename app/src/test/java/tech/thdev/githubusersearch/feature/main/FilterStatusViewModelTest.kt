package tech.thdev.githubusersearch.feature.main

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.domain.model.GitHubSortType
import tech.thdev.githubusersearch.feature.main.model.FilterStatusUiState

class FilterStatusViewModelTest {

    private val viewModel = FilterStatusViewModel()

    @Test
    fun `test initData`() {
        Assertions.assertEquals(FilterStatusUiState.Default, viewModel.filterUiState.value)
    }

    @Test
    fun `test selectSortType`() {
        viewModel.selectSortType(GitHubSortType.FILTER_SORT_DATE_OF_REGISTRATION)
        Assertions.assertEquals(
            FilterStatusUiState(
                sortType = GitHubSortType.FILTER_SORT_DATE_OF_REGISTRATION,
                icon = R.drawable.ic_sort,
            ),
            viewModel.filterUiState.value
        )
    }
}