package tech.thdev.githubusersearch.feature.main.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import tech.thdev.githubusersearch.feature.main.ViewType

@Immutable
data class MainUiState(
    val bottomItems: PersistentList<Bottom>,
) {

    @Immutable
    data class Bottom(
        val title: Int,
        val selected: Boolean,
        val icon: Int,
        val viewType: ViewType,
    )

    companion object {

        val Default = MainUiState(
            bottomItems = persistentListOf(),
        )
    }
}