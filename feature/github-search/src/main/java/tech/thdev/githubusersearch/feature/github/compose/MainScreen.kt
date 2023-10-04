package tech.thdev.githubusersearch.feature.github.compose

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import tech.thdev.githubusersearch.feature.github.MainViewModel
import tech.thdev.githubusersearch.feature.github.ViewType

@Composable
internal fun MainScreen(
    hostMap: PersistentMap<ViewType, @Composable () -> Unit>,
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val selectNavigation by mainViewModel.selectNavigation.collectAsStateWithLifecycle()

    MainScreen(
        hostMap = hostMap,
        navController = navController,
        selectNavigation = selectNavigation,
    )
}

@Composable
private fun MainScreen(
    selectNavigation: ViewType,
    hostMap: PersistentMap<ViewType, @Composable () -> Unit>,
    navController: NavHostController = rememberNavController(),
) {
    LaunchedEffect(selectNavigation) {
        navController.navigate(selectNavigation.name)
    }

    NavHost(
        navController = navController,
        startDestination = selectNavigation.name,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
    ) {
        hostMap.forEach { (viewType, view) ->
            composable(viewType.name) {
                view()
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
internal fun PreviewMainScreen() {
    MainScreen(
        selectNavigation = ViewType.SEARCH,
        hostMap = persistentMapOf(
            ViewType.SEARCH to {
                Text(ViewType.SEARCH.name)
            },
            ViewType.LIKED to {
                Text(ViewType.LIKED.name)
            },
        ),
    )
}