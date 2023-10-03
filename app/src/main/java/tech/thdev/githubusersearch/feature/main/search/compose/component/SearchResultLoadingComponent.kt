package tech.thdev.githubusersearch.feature.main.search.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.design.system.theme.ColorLoadingBackground

@Composable
internal fun SearchResultLoading(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = ColorLoadingBackground)
    ) {
        Text(
            text = stringResource(id = R.string.search_hint),
            fontSize = 16.sp,
            modifier = Modifier
                .padding(20.dp)
        )
    }
}

@Preview
@Composable
internal fun PreviewSearchResultLoading() {
    SearchResultLoading()
}