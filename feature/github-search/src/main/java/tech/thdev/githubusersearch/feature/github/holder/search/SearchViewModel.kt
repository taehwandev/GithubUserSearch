package tech.thdev.githubusersearch.feature.github.holder.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Named
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
import kotlinx.coroutines.launch
import tech.thdev.githubusersearch.design.system.R
import tech.thdev.githubusersearch.domain.github.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.github.model.GitHubSortType
import tech.thdev.githubusersearch.domain.github.model.GitHubUserEntity
import tech.thdev.githubusersearch.feature.github.holder.search.model.SearchUiState
import tech.thdev.githubusersearch.feature.github.holder.search.model.convert.convert

class SearchViewModel @AssistedInject constructor(
    private val gitHubSearchRepository: GitHubSearchRepository,
    @Assisted isTest: Boolean = false,
) : ViewModel() {

    private val _showProgress = MutableStateFlow(false)
    val showProgress: StateFlow<Boolean> get() = _showProgress.asStateFlow()

    private val _searchUiState = MutableStateFlow(SearchUiState.Default)
    val searchUiState: StateFlow<SearchUiState> get() = _searchUiState.asStateFlow()

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
    fun loadData(): Flow<SearchUiState> =
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
                it.convert(searchUiState.value)
            }
            .onEach {
                loadData.value = false
                _searchUiState.value = it
                _showProgress.value = false
            }
            .retry {
                loadData.value = false
                _showProgress.value = false
                _searchUiState.value = searchUiState.value.copy(
                    uiState = SearchUiState.UiState.Empty(message = it.message),
                )
                true
            }

    fun loadMore(visibleItemCount: Int, totalItemCount: Int, firstVisibleItem: Int) {
        if (loadData.value.not() && (firstVisibleItem + visibleItemCount) >= totalItemCount - 10) {
            loadData.value = true
        }
    }

    fun searchKeyword() {
        flowSearchKeyword.tryEmit(searchUiState.value.searchKeyword)
        loadData.value = true
    }

    fun changeSearchKeyword(keyword: String) {
        _searchUiState.value = searchUiState.value.copy(
            searchKeyword = keyword,
        )
    }

    fun selectedLikeChange(item: SearchUiState.UiState.UserItems.Info) = viewModelScope.launch {
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

    fun changeSortType(sortType: GitHubSortType) {
        gitHubSearchRepository.sortList(sortType)
        _searchUiState.value = searchUiState.value.copy(
            sortType = sortType,
            sortIcon = sortType.sortIcon,
        )
    }

    private val GitHubSortType.sortIcon: Int
        get() = when (this) {
            GitHubSortType.FILTER_SORT_NAME -> R.drawable.ic_sort_alphabet
            GitHubSortType.FILTER_SORT_DATE_OF_REGISTRATION -> R.drawable.ic_sort
            else -> R.drawable.ic_sort_numbers
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