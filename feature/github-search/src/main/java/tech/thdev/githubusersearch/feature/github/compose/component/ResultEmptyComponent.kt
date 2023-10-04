package tech.thdev.githubusersearch.feature.github.compose.component

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
import tech.thdev.githubusersearch.design.system.R

@Composable
internal fun ResultEmpty(
    modifier: Modifier = Modifier,
    message: String?,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = message ?: stringResource(id = R.string.message_no_result_user_name),
            fontSize = 16.sp,
            modifier = Modifier
                .padding(20.dp)
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
internal fun PreviewResultEmpty() {
    ResultEmpty(
        message = null,
    )
}