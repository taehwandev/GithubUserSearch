package tech.thdev.githubusersearch.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.model.GitHubUserEntity
import tech.thdev.githubusersearch.feature.main.model.MainListUiState

class LikeChangeViewModel(
    private val gitHubSearchRepository: GitHubSearchRepository,
) : ViewModel() {

    fun selectedLikeChange(item: MainListUiState.UserItems.Info) = viewModelScope.launch {
        if (item.isLike) {
            gitHubSearchRepository.unlikeUserInfo(id = item.id)
        } else {
            gitHubSearchRepository.likeUserInfo(
                GitHubUserEntity(
                    id = item.id,
                    login = item.login,
                    avatarUrl = item.avatarUrl,
                    score = item.score,
                    isLike = false,
                )
            )
        }
    }
}