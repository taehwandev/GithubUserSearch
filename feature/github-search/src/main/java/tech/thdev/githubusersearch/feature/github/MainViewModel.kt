package tech.thdev.githubusersearch.feature.github

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.thdev.githubusersearch.design.system.R
import tech.thdev.githubusersearch.feature.github.model.MainUiState

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _selectNavigation = MutableStateFlow(ViewType.SEARCH)
    val selectNavigation: StateFlow<ViewType> get() = _selectNavigation.asStateFlow()

    private val _mainUiState = MutableStateFlow(
        MainUiState(
            bottomItems = persistentListOf(
                MainUiState.Bottom(
                    title = R.string.title_search,
                    selected = true,
                    icon = R.drawable.ic_search_black_24dp,
                    viewType = ViewType.SEARCH,
                ),
                MainUiState.Bottom(
                    title = R.string.title_liked,
                    selected = false,
                    icon = R.drawable.ic_collections_bookmark_black_24dp,
                    viewType = ViewType.LIKED,
                ),
            )
        )
    )
    val mainUiState: StateFlow<MainUiState> get() = _mainUiState.asStateFlow()

    fun bottomChange(viewType: ViewType) {
        _mainUiState.value = mainUiState.value.copy(
            bottomItems = mainUiState.value.bottomItems.map { item ->
                if (viewType == item.viewType) {
                    item.copy(selected = true)
                } else {
                    item.copy(selected = false)
                }
            }.toPersistentList()
        )
        _selectNavigation.value = viewType
    }
}