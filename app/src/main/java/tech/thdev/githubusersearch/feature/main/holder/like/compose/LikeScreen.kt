package tech.thdev.githubusersearch.feature.main.holder.like.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import tech.thdev.githubusersearch.feature.main.compose.component.ResultEmpty
import tech.thdev.githubusersearch.feature.main.holder.like.LikeViewModel
import tech.thdev.githubusersearch.feature.main.holder.like.compose.component.LikeResult
import tech.thdev.githubusersearch.feature.main.holder.like.model.LikeUiState

@Composable
internal fun LikeScreen(
    likeViewModel: LikeViewModel,
) {
    val likeUiState by likeViewModel.likeUiState.collectAsStateWithLifecycle()

    LikeScreen(
        likeUiState = likeUiState,
        onClick = likeViewModel::selectedLikeChange,
    )
}

@Composable
private fun LikeScreen(
    likeUiState: LikeUiState,
    onClick: (LikeUiState.UserItems.Info) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        when (likeUiState) {
            is LikeUiState.Empty -> {
                ResultEmpty(
                    message = likeUiState.message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            is LikeUiState.UserItems -> {
                LikeResult(
                    userItems = likeUiState.items,
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
internal fun PreviewLikeScreenEmpty() {
    LikeScreen(
        likeUiState = LikeUiState.Empty(
            message = "Empty result",
        ),
        onClick = {},
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
internal fun PreviewLikeScreenItems() {
    LikeScreen(
        likeUiState = LikeUiState.UserItems(
            items = persistentListOf(
                LikeUiState.UserItems.Info.Default.copy(
                    login = "a",
                    score = 0.14,
                ),
                LikeUiState.UserItems.Info.Default.copy(
                    login = "b",
                    score = 1.0,
                    isLike = true,
                ),
            ),
        ),
        onClick = {},
    )
}