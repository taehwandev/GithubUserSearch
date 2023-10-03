package tech.thdev.githubusersearch.feature.main.compose

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import tech.thdev.githubusersearch.feature.main.MainViewModel
import tech.thdev.githubusersearch.feature.main.ViewType

@Composable
internal fun MainScreen(
    modifier: Modifier,
    hostMap: PersistentMap<ViewType, @Composable () -> Unit>,
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val selectNavigation by mainViewModel.selectNavigation.collectAsStateWithLifecycle()

    MainScreen(
        modifier = modifier,
        hostMap = hostMap,
        navController = navController,
        selectNavigation = selectNavigation,
    )
}

@Composable
private fun MainScreen(
    modifier: Modifier,
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
        modifier = modifier
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
        modifier = Modifier
    )
}