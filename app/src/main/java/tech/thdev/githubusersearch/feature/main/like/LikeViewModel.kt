package tech.thdev.githubusersearch.feature.main.like

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
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.feature.main.model.MainListUiState
import tech.thdev.githubusersearch.feature.main.model.convert.convert

class LikeViewModel @AssistedInject constructor(
    private val gitHubSearchRepository: GitHubSearchRepository,
    @Assisted isTest: Boolean = false,
) : ViewModel() {

    @AssistedFactory
    interface LikeAssistedFactory {

        fun create(@Named("is_test") isTest: Boolean): LikeViewModel
    }

    private val _mainListUiState = MutableStateFlow<MainListUiState>(MainListUiState.UserItems.Default)
    val mainListUiState: StateFlow<MainListUiState> get() = _mainListUiState.asStateFlow()

    init {
        if (isTest.not()) {
            loadData()
                .launchIn(viewModelScope)
        }
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