package tech.thdev.githubusersearch.feature.main.search

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.model.GitHubSortType
import tech.thdev.githubusersearch.domain.model.GitHubUserEntity
import tech.thdev.githubusersearch.feature.main.model.MainListUiState

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    private val gitHubSearchRepository = mock<GitHubSearchRepository>()

    private val viewModel = SearchViewModel(
        gitHubSearchRepository = gitHubSearchRepository,
        test = true,
    )

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initData`() {
        Assertions.assertFalse(viewModel.showProgress.value)
        Assertions.assertEquals(MainListUiState.UserItems.Default, viewModel.mainListUiState.value)
        Assertions.assertFalse(viewModel.loadData.value)
        Assertions.assertEquals("", viewModel.flowSearchKeyword.value)
    }

    @Test
    fun `test loadData`() = runTest {
        val mockSearchKeyword = "keyword"
        val mockResponse = listOf(
            GitHubUserEntity.Default.copy(
                login = "name",
                score = 0.14,
            ),
            GitHubUserEntity.Default.copy(
                login = "name two",
                score = 0.14,
            ),
        )
        whenever(
            gitHubSearchRepository.flowLoadData(
                searchKeyword = mockSearchKeyword,
                perPage = 30,
            )
        ).thenReturn(flowOf(mockResponse))

        viewModel.loadData()
            .test {
                expectNoEvents()

                viewModel.updateKeyword(mockSearchKeyword)

                val convert = MainListUiState.UserItems(
                    items = listOf(
                        MainListUiState.UserItems.Info.Default.copy(
                            login = "name",
                            score = 0.14,
                        ),
                        MainListUiState.UserItems.Info.Default.copy(
                            login = "name two",
                            score = 0.14,
                        ),
                    ),
                )
                Assertions.assertEquals(convert, awaitItem())
                Assertions.assertEquals(convert, viewModel.mainListUiState.value)

                verify(gitHubSearchRepository).flowLoadData(
                    searchKeyword = mockSearchKeyword,
                    perPage = 30,
                )

                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test loadData - fail case`() = runTest(dispatcher) {
        val mockSearchKeyword = "keyword"

        val fail = flowOf(false)
            .map {
                throw Exception("message")
            }

        whenever(
            gitHubSearchRepository.flowLoadData(
                searchKeyword = mockSearchKeyword,
                perPage = 30,
            )
        ).thenReturn(fail)

        viewModel.updateKeyword(mockSearchKeyword)

        viewModel.loadData()
            .test {
                val convert = MainListUiState.Error("message")
                Assertions.assertEquals(convert, viewModel.mainListUiState.value)

                verify(gitHubSearchRepository).flowLoadData(
                    searchKeyword = mockSearchKeyword,
                    perPage = 30,
                )

                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test updateKeyword`() {
        val newValue = "new"
        viewModel.updateKeyword(newValue)
        Assertions.assertTrue(viewModel.loadData.value)
        Assertions.assertEquals("new", viewModel.flowSearchKeyword.value)
    }

    @Test
    fun `test changeSort`() = runTest {
        viewModel.changeSort(GitHubSortType.FILTER_SORT_NAME)
        verify(gitHubSearchRepository).sortList(GitHubSortType.FILTER_SORT_NAME)
    }

    @Test
    fun `test loadMore`() {
        viewModel.loadMore(10, 12, 8)
        Assertions.assertTrue(viewModel.loadData.value)
    }
}