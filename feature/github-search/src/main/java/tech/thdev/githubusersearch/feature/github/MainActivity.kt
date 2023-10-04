package tech.thdev.githubusersearch.feature.github

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.collections.immutable.persistentMapOf
import tech.thdev.githubusersearch.design.system.theme.MyApplicationTheme
import tech.thdev.githubusersearch.feature.github.compose.MainScreen
import tech.thdev.githubusersearch.feature.github.holder.like.LikeComposableHolder
import tech.thdev.githubusersearch.feature.github.holder.like.LikeViewModel
import tech.thdev.githubusersearch.feature.github.holder.search.SearchComposableHolder
import tech.thdev.githubusersearch.feature.github.holder.search.SearchViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var searchViewModelFactory: SearchViewModel.SearchAssistedFactory

    private val searchViewModel by viewModels<SearchViewModel> {
        SearchViewModel.provideFactory(searchViewModelFactory, false)
    }

    @Inject
    lateinit var likeViewModelFactory: LikeViewModel.LikeAssistedFactory

    private val likeViewModel by viewModels<LikeViewModel> {
        LikeViewModel.provideFactory(likeViewModelFactory, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val mainUiState by mainViewModel.mainUiState.collectAsStateWithLifecycle()

                val bottomBar: @Composable () -> Unit = {
                    NavigationBar {
                        mainUiState.bottomItems.forEach { item ->
                            NavigationBarItem(
                                selected = item.selected,
                                onClick = {
                                    mainViewModel.bottomChange(item.viewType)
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = item.icon),
                                        contentDescription = null,
                                    )
                                },
                                label = {
                                    Text(text = stringResource(id = item.title))
                                }
                            )
                        }
                    }
                }

                MainScreen(
                    navController = navController,
                    hostMap = persistentMapOf(
                        ViewType.SEARCH to {
                            SearchComposableHolder(
                                bottomBar = bottomBar,
                                searchViewModel = searchViewModel,
                            )
                        },
                        ViewType.LIKED to {
                            LikeComposableHolder(
                                bottomBar = bottomBar,
                                likeViewModel = likeViewModel,
                            )
                        },
                    ),
                )
            }
        }
    }
}
