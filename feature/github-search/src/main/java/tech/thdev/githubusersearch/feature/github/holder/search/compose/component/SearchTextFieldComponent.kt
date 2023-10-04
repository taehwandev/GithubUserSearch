package tech.thdev.githubusersearch.feature.github.holder.search.compose.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.thdev.githubusersearch.design.system.R

@Composable
internal fun SearchTextField(
    value: String,
    hint: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        TextField(
            value = value,
            placeholder = {
                Text(text = hint)
            },
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            modifier = Modifier
                .weight(1f)
        )

        IconButton(
            onClick = onClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search_black_24dp),
                contentDescription = "search",
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
internal fun PreviewSearchTextField() {
    SearchTextField(
        value = "",
        hint = "Search user name",
        onValueChange = {},
        onClick = {},
    )
}