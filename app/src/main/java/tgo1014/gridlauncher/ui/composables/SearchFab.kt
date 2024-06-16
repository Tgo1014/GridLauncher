package tgo1014.gridlauncher.ui.composables

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.DecorationBox
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tgo1014.gridlauncher.R
import tgo1014.gridlauncher.app.Constants.defaultRadius
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFab(
    modifier: Modifier = Modifier,
    searchText: String,
    buttonState: SearchFabState,
    cornerRadius: Int = defaultRadius,
    onCloseClicked: () -> Unit = {},
    onSearchTextChanged: (String) -> Unit = {},
    onButtonClicked: () -> Unit = {},
) = BoxWithConstraints(modifier) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val fabSize = 56.dp
    val itemsSize = 32.dp
    val shape = RoundedCornerShape(cornerRadius)
    val interactionSource = remember { MutableInteractionSource() }
    val transition = updateTransition(buttonState, label = "Width")
    val height by transition.animateDp(
        targetValueByState = {
            when (it) {
                SearchFabState.FAB -> fabSize
                SearchFabState.SEARCH -> 65.dp
            }
        }, label = "Height"
    )
    val width by transition.animateDp(
        targetValueByState = {
            if (it == SearchFabState.FAB) fabSize else maxWidth
        },
        transitionSpec = { tween() },
        label = "width"
    )
    Surface(
        shape = shape,
        color = MaterialTheme.colorScheme.secondary,
        shadowElevation = 6.dp, // Fab default
        modifier = Modifier
            .width(width)
            .height(height)
            .align(Alignment.BottomEnd)
            .clickable { onButtonClicked() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.requiredSize(itemsSize),
                tint = MaterialTheme.colorScheme.onSecondary
            )
            val focusRequest = remember { FocusRequester() }
            if (buttonState == SearchFabState.FAB) {
                LocalFocusManager.current.clearFocus(true)
            } else {
                val singleLine = true
                val colors = TextFieldDefaults.colors().copy(
                    cursorColor = MaterialTheme.colorScheme.tertiary,
                    focusedContainerColor = Color.Transparent,
                    focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onSecondary,
                )
                val selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                )
                SideEffect { focusRequest.requestFocus() }
                CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
                    BasicTextField(
                        value = searchText,
                        onValueChange = { onSearchTextChanged(it) },
                        interactionSource = interactionSource,
                        singleLine = singleLine,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondary),
                        textStyle = TextStyle.Default.copy(
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        ),
                        decorationBox = {
                            DecorationBox(
                                value = searchText,
                                innerTextField = it,
                                enabled = true,
                                interactionSource = interactionSource,
                                singleLine = singleLine,
                                visualTransformation = VisualTransformation.None,
                                contentPadding = PaddingValues(horizontal = 0.dp),
                                colors = colors,
                                label = { Text(text = stringResource(R.string.search)) },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            keyboardController?.hide()
                                            onCloseClicked()
                                        },
                                        content = {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSecondary,
                                            )
                                        }
                                    )
                                },
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .fillMaxWidth()
                            .requiredHeight(45.dp)
                            .focusRequester(focusRequest)
                            .indicatorLine(
                                enabled = true,
                                isError = false,
                                interactionSource = interactionSource,
                                colors = colors
                            )
                    )
                }
            }
        }
    }
}

enum class SearchFabState {
    FAB, SEARCH;

    fun toggle() = if (this == FAB) SEARCH else FAB
}

@Preview
@Composable
private fun SearchFabPreviewFab() = GridLauncherTheme {
    var state by remember { mutableStateOf(SearchFabState.FAB) }
    SearchFab(buttonState = state, searchText = "") {
        state = if (state == SearchFabState.FAB) SearchFabState.SEARCH else SearchFabState.FAB
    }
}

@Preview
@Composable
private fun SearchFabPreviewFabLoading() = GridLauncherTheme {
    var state by remember { mutableStateOf(SearchFabState.FAB) }
    SearchFab(buttonState = state, searchText = "") {
        state = if (state == SearchFabState.FAB) SearchFabState.SEARCH else SearchFabState.FAB
    }
}

@Preview
@Composable
private fun SearchFabPreviewSearch() = GridLauncherTheme {
    var state by remember { mutableStateOf(SearchFabState.FAB) }
    SearchFab(buttonState = SearchFabState.SEARCH, searchText = "123") {
        state = if (state == SearchFabState.FAB) SearchFabState.SEARCH else SearchFabState.FAB
    }
}

@Preview
@Composable
private fun SearchFabPreviewSearchLoading() = GridLauncherTheme {
    var state by remember { mutableStateOf(SearchFabState.FAB) }
    SearchFab(buttonState = SearchFabState.SEARCH, searchText = "123") {
        state = if (state == SearchFabState.FAB) SearchFabState.SEARCH else SearchFabState.FAB
    }
}