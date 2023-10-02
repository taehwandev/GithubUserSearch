package tech.thdev.githubusersearch.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchQueryViewModel : ViewModel() {

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> get() = _searchQuery

    fun setSearchQuery(query: String?) {
        _searchQuery.value = query ?: ""
    }
}