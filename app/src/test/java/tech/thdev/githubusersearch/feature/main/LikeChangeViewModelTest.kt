package tech.thdev.githubusersearch.feature.main

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.model.GitHubUserEntity
import tech.thdev.githubusersearch.feature.main.model.MainListUiState

@OptIn(ExperimentalCoroutinesApi::class)
class LikeChangeViewModelTest {

    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    private val gitHubSearchRepository = mock<GitHubSearchRepository>()

    private val viewModel = LikeChangeViewModel(
        gitHubSearchRepository = gitHubSearchRepository,
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
    fun `test selectedLikeChange`() = runTest {
        val mockItem = MainListUiState.UserItems.Info.Default.copy(id = 1, login = "aa")
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

        val mockItemTwo = MainListUiState.UserItems.Info.Default.copy(id = 1, login = "aa", isLike = true)
        viewModel.selectedLikeChange(mockItemTwo)
        verify(gitHubSearchRepository).unlikeUserInfo(id = 1)
    }
}