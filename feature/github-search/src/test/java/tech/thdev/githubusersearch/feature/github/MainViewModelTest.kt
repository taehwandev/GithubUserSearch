package tech.thdev.githubusersearch.feature.github

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.thdev.githubusersearch.design.system.R
import tech.thdev.githubusersearch.feature.github.MainViewModel
import tech.thdev.githubusersearch.feature.github.ViewType
import tech.thdev.githubusersearch.feature.github.model.MainUiState

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    private val viewModel = MainViewModel()

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initData`() {
        Assertions.assertEquals(ViewType.SEARCH, viewModel.selectNavigation.value)
        Assertions.assertEquals(
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
            ),
            viewModel.mainUiState.value
        )
    }

    @Test
    fun `test bottomChange`() {
        viewModel.bottomChange(ViewType.LIKED)

        Assertions.assertEquals(
            MainUiState(
                bottomItems = persistentListOf(
                    MainUiState.Bottom(
                        title = R.string.title_search,
                        selected = false,
                        icon = R.drawable.ic_search_black_24dp,
                        viewType = ViewType.SEARCH,
                    ),
                    MainUiState.Bottom(
                        title = R.string.title_liked,
                        selected = true,
                        icon = R.drawable.ic_collections_bookmark_black_24dp,
                        viewType = ViewType.LIKED,
                    ),
                )
            ),
            viewModel.mainUiState.value
        )
    }
}