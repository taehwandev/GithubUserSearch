package tech.thdev.githubusersearch.feature.main

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.domain.model.GitHubSortType
import tech.thdev.githubusersearch.feature.main.model.FilterStatusUiState

class FilterStatusViewModelTest {

    private val viewModel = FilterStatusViewModel()

    @Test
    fun `test selectSortType`() = runTest {
        viewModel.filterUiState
            .test {
                Assertions.assertEquals(FilterStatusUiState.Default, awaitItem())

                viewModel.selectSortType(GitHubSortType.FILTER_SORT_NAME)
                Assertions.assertEquals(
                    FilterStatusUiState(
                        sortType = GitHubSortType.FILTER_SORT_NAME,
                        icon = R.drawable.ic_sort_alphabet,
                    ),
                    awaitItem()
                )

                cancelAndConsumeRemainingEvents()
            }
    }
}