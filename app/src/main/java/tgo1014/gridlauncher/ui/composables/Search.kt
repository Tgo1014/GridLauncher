package tgo1014.gridlauncher.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@Composable
fun Search(
    text: String = "",
    color: Color = MaterialTheme.colorScheme.secondaryContainer,
    onTextChanged: (String) -> Unit = {},
    onClearPressed: () -> Unit = {},
) = GridLauncherTheme {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = text,
        onValueChange = onTextChanged,
        placeholder = { Text("Search apps", color = color) },
        singleLine = true,
        trailingIcon = {
            IconButton(
                onClick = { if (text.isNotBlank()) onClearPressed() },
                content = {
                    val icon = if (text.isBlank()) {
                        Icons.Default.Search
                    } else {
                        Icons.Default.Clear
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color
                    )
                }
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = color,
            unfocusedIndicatorColor = color,
            unfocusedLabelColor = color,
            focusedTextColor = color,
            focusedLabelColor = color,
            focusedTrailingIconColor = color,
            cursorColor = color,
            unfocusedTextColor = color,
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
@Preview
private fun Preview() = GridLauncherTheme {
    Search()
}

@Composable
@Preview
private fun PreviewNotEmpty() = GridLauncherTheme {
    Search("FooBar")
}