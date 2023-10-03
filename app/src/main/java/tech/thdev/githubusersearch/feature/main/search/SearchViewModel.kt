package tech.thdev.githubusersearch.feature.main.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.model.GitHubSortType
import tech.thdev.githubusersearch.feature.main.model.MainListUiState
import tech.thdev.githubusersearch.feature.main.model.convert.convert
import javax.inject.Named

class SearchViewModel @AssistedInject constructor(
    private val gitHubSearchRepository: GitHubSearchRepository,
    @Assisted isTest: Boolean = false,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery.asStateFlow()

    private val _showProgress = MutableStateFlow(false)
    val showProgress: StateFlow<Boolean> get() = _showProgress.asStateFlow()

    private val _mainListUiState = MutableStateFlow<MainListUiState>(MainListUiState.Empty.Default)
    val mainListUiState: StateFlow<MainListUiState> get() = _mainListUiState.asStateFlow()

    @VisibleForTesting
    val loadData = MutableStateFlow(false)

    @VisibleForTesting
    val flowSearchKeyword = MutableStateFlow("")

    init {
        if (isTest.not()) {
            loadData()
                .launchIn(viewModelScope)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @VisibleForTesting
    fun loadData(): Flow<MainListUiState> =
        loadData
            .filter { it }
            .onEach {
                if (flowSearchKeyword.value.isNotEmpty()) {
                    _showProgress.value = true
                }
            }
            .flatMapLatest { flowSearchKeyword }
            .filter { searchKeyword ->
                searchKeyword.isNotEmpty()
            }
            .flatMapLatest { searchKeyword ->
                gitHubSearchRepository.flowLoadData(
                    searchKeyword = searchKeyword,
                    perPage = 30,
                )
            }
            .map {
                it.convert()
            }
            .onEach {
                loadData.value = false
                _mainListUiState.value = it
                _showProgress.value = false
            }
            .retry {
                loadData.value = false
                _showProgress.value = false
                _mainListUiState.value = MainListUiState.Empty(message = it.message)
                true
            }

    fun updateKeyword(keyword: String) {
        flowSearchKeyword.tryEmit(keyword)
        loadData.value = true
    }

    fun changeSort(sort: GitHubSortType) {
        gitHubSearchRepository.sortList(sort)
    }

    fun loadMore(visibleItemCount: Int, totalItemCount: Int, firstVisibleItem: Int) {
        if (loadData.value.not() && (firstVisibleItem + visibleItemCount) >= totalItemCount - 10) {
            loadData.value = true
        }
    }

    fun setSearchQuery(query: String?) {
        _searchQuery.tryEmit(query ?: "")
    }

    override fun onCleared() {
        gitHubSearchRepository.clear()
    }

    @AssistedFactory
    interface SearchAssistedFactory {

        fun create(@Named("is_test") isTest: Boolean): SearchViewModel
    }

    companion object {

        fun provideFactory(
            assistedFactory: SearchAssistedFactory,
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