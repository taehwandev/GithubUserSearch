package tech.thdev.githubusersearch.feature.main.like

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.model.GitHubUserEntity
import tech.thdev.githubusersearch.feature.main.model.MainListUiState
import tech.thdev.githubusersearch.feature.main.model.convert.convert

class LikeViewModel(
    private val gitHubSearchRepository: GitHubSearchRepository,
) : ViewModel() {

    private val _mainListUiState = MutableStateFlow<MainListUiState>(MainListUiState.UserItems.Default)
    val mainListUiState: StateFlow<MainListUiState> get() = _mainListUiState.asStateFlow()

    init {
        loadData()
            .launchIn(viewModelScope)
    }

    @VisibleForTesting
    fun loadData(): Flow<MainListUiState> =
        gitHubSearchRepository.flowLoadLikedData()
            .flowOn(Dispatchers.Default)
            .map {
                it.convert()
            }
            .onEach {
                _mainListUiState.value = it
            }

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