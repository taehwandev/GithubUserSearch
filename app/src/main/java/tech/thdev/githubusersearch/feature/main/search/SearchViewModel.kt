package tech.thdev.githubusersearch.feature.main.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import tech.thdev.githubusersearch.domain.GitHubSearchRepository
import tech.thdev.githubusersearch.domain.model.GitHubSortType
import tech.thdev.githubusersearch.domain.model.GitHubUserEntity
import tech.thdev.githubusersearch.feature.main.model.MainListUiState
import tech.thdev.githubusersearch.feature.main.model.convert.convert
import tech.thdev.githubusersearch.util.NoNetworkException

class SearchViewModel(
    private val gitHubSearchRepository: GitHubSearchRepository,
) : ViewModel() {

    private val _showProgress = MutableStateFlow(false)
    val showProgress: StateFlow<Boolean> get() = _showProgress.asStateFlow()

    private val _mainListUiState = MutableStateFlow<MainListUiState>(MainListUiState.UserItems.Default)
    val mainListUiState: StateFlow<MainListUiState> get() = _mainListUiState.asStateFlow()

    private var isLoad = false

    private val loadData = MutableStateFlow(false)

    private val flowSearchKeyword = MutableStateFlow("")

    init {
        loadData()
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @VisibleForTesting
    fun loadData(): Flow<MainListUiState> =
        loadData
            .filter { it && isLoad.not() }
            .onEach {
                loadData.value = false
                if (flowSearchKeyword.value.isNotEmpty()) {
                    isLoad = true
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
            .flowOn(Dispatchers.Default)
            .map {
                it.convert()
            }
            .onEach {
                _mainListUiState.value = it
                _showProgress.value = false
            }
            .retryWhen { cause, _ ->
                _showProgress.value = false

                when (cause) {
                    is NoNetworkException -> {
                        _mainListUiState.value = MainListUiState.NetworkFail
                    }

                    else -> {
                        _mainListUiState.value = MainListUiState.Error(
                            message = cause.message,
                        )
                    }
                }
                true
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

    fun search(searchQuery: String) {
        flowSearchKeyword.tryEmit(searchQuery)
        loadData.value = true
    }

    fun changeSort(sort: GitHubSortType) {
        gitHubSearchRepository.sortList(sort)
    }

    fun loadMore(visibleItemCount: Int, totalItemCount: Int, firstVisibleItem: Int) {
        if (isLoad.not() && (firstVisibleItem + visibleItemCount) >= totalItemCount - 10) {
            loadData.value = true
        }
    }

    override fun onCleared() {
        gitHubSearchRepository.clear()
    }
}