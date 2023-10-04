package tech.thdev.githubusersearch.feature.github.holder.search

import app.cash.turbine.test
import kotlinx.collections.immutable.persistentListOf
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
import tech.thdev.githubusersearch.design.system.R
import tech.thdev.githubusersearch.domain.github.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.github.model.GitHubSortType
import tech.thdev.githubusersearch.domain.github.model.GitHubUserEntity
import tech.thdev.githubusersearch.feature.github.holder.search.model.SearchUiState

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    private val gitHubSearchRepository = mock<GitHubSearchRepository>()

    private val viewModel = SearchViewModel(
        gitHubSearchRepository = gitHubSearchRepository,
        isTest = true,
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
        Assertions.assertEquals(SearchUiState.Default, viewModel.searchUiState.value)
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

                viewModel.changeSearchKeyword(mockSearchKeyword)
                viewModel.searchKeyword()

                val convert = SearchUiState.Default.copy(
                    searchKeyword = mockSearchKeyword,
                    uiState = SearchUiState.UiState.UserItems(
                        items = persistentListOf(
                            SearchUiState.UiState.UserItems.Info.Default.copy(
                                login = "name",
                                score = 0.14,
                            ),
                            SearchUiState.UiState.UserItems.Info.Default.copy(
                                login = "name two",
                                score = 0.14,
                            ),
                        ),
                    ),
                )
                Assertions.assertEquals(convert, awaitItem())
                Assertions.assertEquals(convert, viewModel.searchUiState.value)

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

        viewModel.changeSearchKeyword(mockSearchKeyword)
        viewModel.searchKeyword()

        viewModel.loadData()
            .test {
                val convert = SearchUiState.Default.copy(
                    searchKeyword = mockSearchKeyword,
                    uiState = SearchUiState.UiState.Empty("message"),
                )
                Assertions.assertEquals(convert, viewModel.searchUiState.value)

                verify(gitHubSearchRepository).flowLoadData(
                    searchKeyword = mockSearchKeyword,
                    perPage = 30,
                )

                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test changeSearchKeyword`() {
        val newValue = "new"
        viewModel.changeSearchKeyword(newValue)
        viewModel.searchKeyword()
        Assertions.assertTrue(viewModel.loadData.value)
        Assertions.assertEquals("new", viewModel.flowSearchKeyword.value)
    }

    @Test
    fun `test loadMore`() {
        viewModel.loadMore(10, 12, 8)
        Assertions.assertTrue(viewModel.loadData.value)
    }

    @Test
    fun `test selectedLikeChange`() = runTest {
        val mockItem = SearchUiState.UiState.UserItems.Info.Default.copy(id = 1, login = "aa")
        viewModel.selectedLikeChange(mockItem)
        verify(gitHubSearchRepository).likeUserInfo(
            GitHubUserEntity(
                id = 1,
                login = "aa",
                avatarUrl = "",
                score = 0.0,
                isLike = false,
            )
        )

        val mockItemTwo = SearchUiState.UiState.UserItems.Info.Default.copy(id = 1, login = "aa", isLike = true)
        viewModel.selectedLikeChange(mockItemTwo)
        verify(gitHubSearchRepository).unlikeUserInfo(id = 1)
    }

    @Test
    fun `test changeSortType`() {
        viewModel.changeSortType(GitHubSortType.FILTER_SORT_DATE_OF_REGISTRATION)
        Assertions.assertEquals(
            SearchUiState.Default.copy(
                sortType = GitHubSortType.FILTER_SORT_DATE_OF_REGISTRATION,
                sortIcon = R.drawable.ic_sort,
            ),
            viewModel.searchUiState.value
        )
        verify(gitHubSearchRepository).sortList(GitHubSortType.FILTER_SORT_NAME)
    }
}