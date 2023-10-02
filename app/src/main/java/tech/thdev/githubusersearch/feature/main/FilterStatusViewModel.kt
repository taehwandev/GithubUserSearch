package tech.thdev.githubusersearch.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.domain.model.GitHubSortType
import tech.thdev.githubusersearch.feature.main.model.FilterStatusUiState

class FilterStatusViewModel : ViewModel() {

    private val _filterUiState = MutableLiveData(FilterStatusUiState.Default)
    val filterUiState: LiveData<FilterStatusUiState> get() = _filterUiState

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