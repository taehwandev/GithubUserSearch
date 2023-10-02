package tech.thdev.githubusersearch.feature.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.domain.model.GitHubSortType
import tech.thdev.githubusersearch.feature.main.model.FilterStatusUiState

@HiltViewModel
class FilterStatusViewModel @Inject constructor() : ViewModel() {

    private val _filterUiState = MutableStateFlow(FilterStatusUiState.Default)
    val filterUiState: StateFlow<FilterStatusUiState> get() = _filterUiState.asStateFlow()

    private val GitHubSortType.sortIcon: Int
        get() = when (this) {
            GitHubSortType.FILTER_SORT_NAME -> R.drawable.ic_sort_alphabet
            GitHubSortType.FILTER_SORT_DATE_OF_REGISTRATION -> R.drawable.ic_sort
            else -> R.drawable.ic_sort_numbers
        }

    fun selectSortType(sortType: GitHubSortType) {
        _filterUiState.value = FilterStatusUiState(
            sortType = sortType,
            icon = sortType.sortIcon,
        )
    }
}