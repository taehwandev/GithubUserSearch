package tech.thdev.githubusersearch.feature.main.holder.like

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tech.thdev.githubusersearch.feature.main.holder.like.compose.LikeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LikeComposableHolder(
    bottomBar: @Composable () -> Unit,
    likeViewModel: LikeViewModel,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "GitHubUserSearch")
                },
            )
        },
        bottomBar = bottomBar,
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            LikeScreen(likeViewModel = likeViewModel)
        }
    }
}