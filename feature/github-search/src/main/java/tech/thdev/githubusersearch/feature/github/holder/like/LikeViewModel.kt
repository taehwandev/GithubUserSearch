package tech.thdev.githubusersearch.feature.github.holder.like

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Named
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
import tech.thdev.githubusersearch.domain.github.GitHubSearchRepository
import tech.thdev.githubusersearch.feature.github.holder.like.model.LikeUiState
import tech.thdev.githubusersearch.feature.github.holder.like.model.convert.convert

class LikeViewModel @AssistedInject constructor(
    private val gitHubSearchRepository: GitHubSearchRepository,
    @Assisted isTest: Boolean = false,
) : ViewModel() {

    @AssistedFactory
    interface LikeAssistedFactory {

        fun create(@Named("is_test") isTest: Boolean): LikeViewModel
    }

    private val _likeUiState = MutableStateFlow<LikeUiState>(LikeUiState.Empty.Default)
    val likeUiState: StateFlow<LikeUiState> get() = _likeUiState.asStateFlow()

    init {
        if (isTest.not()) {
            loadData()
                .launchIn(viewModelScope)
        }
    }

    @VisibleForTesting
    fun loadData(): Flow<LikeUiState> =
        gitHubSearchRepository.flowLoadLikedData()
            .flowOn(Dispatchers.Default)
            .map {
                it.convert()
            }
            .onEach {
                _likeUiState.value = it
            }

    fun selectedLikeChange(item: LikeUiState.UserItems.Info) = viewModelScope.launch {
        gitHubSearchRepository.unlikeUserInfo(id = item.id)
    }

    companion object {

        fun provideFactory(
            assistedFactory: LikeAssistedFactory,
            isTest: Boolean,
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {

                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return assistedFactory.create(isTest) as T
                }
            }
    }
}