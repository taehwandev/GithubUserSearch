package tech.thdev.githubusersearch.feature.main.like

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import tech.thdev.githubusersearch.domain.model.GitHubUserEntity
import tech.thdev.githubusersearch.feature.main.model.MainListUiState

@OptIn(ExperimentalCoroutinesApi::class)
class LikeViewModelTest {

    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    private val gitHubSearchRepository = mock<GitHubSearchRepository>()

    private val viewModel = LikeViewModel(
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
        Assertions.assertEquals(MainListUiState.UserItems.Default, viewModel.mainListUiState.value)
    }

    @Test
    fun `test loadData`() = runTest {
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
            gitHubSearchRepository.flowLoadLikedData()
        ).thenReturn(flowOf(mockResponse))

        viewModel.loadData()
            .test {
                expectNoEvents()

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

                verify(gitHubSearchRepository).flowLoadLikedData()

                cancelAndConsumeRemainingEvents()
            }
    }
}