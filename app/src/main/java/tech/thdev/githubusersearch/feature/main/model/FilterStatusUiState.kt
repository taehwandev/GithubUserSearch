package tech.thdev.githubusersearch.feature.main.model

import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.domain.model.GitHubSortType

data class FilterStatusUiState(
    val sortType: GitHubSortType,
    val icon: Int,
) {

    companion object {

        val Default = FilterStatusUiState(
            sortType = GitHubSortType.FILTER_SORT_DEFAULT,
            icon = R.drawable.ic_sort_numbers,
        )
    }
}