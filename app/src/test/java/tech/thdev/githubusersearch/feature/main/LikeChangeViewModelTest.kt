package tech.thdev.githubusersearch.feature.main

import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.model.GitHubUserEntity
import tech.thdev.githubusersearch.feature.main.model.MainListUiState

class LikeChangeViewModelTest {

    private val gitHubSearchRepository = mock<GitHubSearchRepository>()

    private val viewModel = LikeChangeViewModel(
        gitHubSearchRepository = gitHubSearchRepository,
    )

    @Test
    fun `test selectedLikeChange`() {
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