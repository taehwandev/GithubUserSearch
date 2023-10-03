package tech.thdev.githubusersearch.feature.main.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.design.system.GitAsyncImage

@Composable
internal fun SearchResultItem(
    login: String,
    avatarUrl: String,
    score: Double,
    isLike: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
        ) {
            GitAsyncImage(
                imageUrl = avatarUrl,
                modifier = Modifier
                    .size(65.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp, end = 12.dp)
            ) {
                Text(
                    text = login,
                    fontSize = 25.sp,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = score.toString(),
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (isLike) {
                Image(
                    painter = painterResource(id = R.drawable.ic_favorite_red_24dp),
                    contentDescription = "liked",
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_favorite_border_red_24dp),
                    contentDescription = "unliked",
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
internal fun PreviewSearchResultItem() {
    Column {
        SearchResultItem(
            login = "User name",
            avatarUrl = "",
            score = 0.0,
            isLike = true,
            onClick = {},
        )

        SearchResultItem(
            login = "User name two",
            avatarUrl = "",
            score = 0.0,
            isLike = false,
            onClick = {},
        )
    }
}