package tech.thdev.githubusersearch.feature.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchQueryViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: SharedFlow<String> get() = _searchQuery.asStateFlow()

    fun setSearchQuery(query: String?) {
        _searchQuery.tryEmit(query ?: "")
    }
}