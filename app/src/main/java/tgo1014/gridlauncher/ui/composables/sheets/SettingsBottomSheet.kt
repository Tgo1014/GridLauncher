package tgo1014.gridlauncher.ui.composables.sheets

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tgo1014.gridlauncher.domain.models.TileSettings
import tgo1014.gridlauncher.ui.models.SettingsEvent
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheet(
    tileSettings: TileSettings,
    isShowing: Boolean,
    onSettingsEvent: (SettingsEvent) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()
    LaunchedEffect(isShowing) {
        if (isShowing) sheetState.show() else sheetState.hide()
    }
    if (isShowing) {
        ModalBottomSheet(
            scrimColor = Color.Transparent,
            onDismissRequest = { onSettingsEvent(SettingsEvent.OnSettingsSheetDismissed) },
            sheetState = sheetState
        ) {
            SettingsBottomSheet(
                tileSettings = tileSettings,
                onSettingsEvent = onSettingsEvent,
            )
        }
    }
}

@Composable
private fun SettingsBottomSheet(
    tileSettings: TileSettings = TileSettings(),
    onSettingsEvent: (SettingsEvent) -> Unit = {},
) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { wallpaperUri ->
                onSettingsEvent(SettingsEvent.OnWallpaperPicked(wallpaperUri))
            }
        }
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Tile Flipping Enabled:", modifier = Modifier.weight(1f))
            Switch(
                checked = tileSettings.isTileFlipEnabled,
                onCheckedChange = {
                    val newSettings = tileSettings.copy(isTileFlipEnabled = it)
                    onSettingsEvent(SettingsEvent.OnSettingsUpdated(newSettings))
                }
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Hide app labels:", modifier = Modifier.weight(1f))
            Switch(
                checked = tileSettings.isAppLabelsHidden,
                onCheckedChange = {
                    val newSettings = tileSettings.copy(isAppLabelsHidden = it)
                    onSettingsEvent(SettingsEvent.OnSettingsUpdated(newSettings))
                },
            )
        }
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Enable tile transparency:", modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                if (tileSettings.isTransparencyEnabled) {
                    Button(
                        onClick = { onSettingsEvent(SettingsEvent.OnWallpaperRemoved) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Remove Image")
                    }
                } else {
                    Button(
                        onClick = {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Pick Image")
                    }
                }
            }
            Text(
                text = "To enable transparency you need to pick your wallpaper manually",
                style = MaterialTheme.typography.labelSmall
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Corners:", modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )
            Text(text = tileSettings.cornerRadius.toString())
            Slider(
                value = tileSettings.cornerRadius.toFloat(),
                onValueChange = {
                    onSettingsEvent(SettingsEvent.OnSettingsUpdated(tileSettings.copy(cornerRadius = it.toInt())))
                },
                valueRange = 0f..50f,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = false, backgroundColor = 0xFFFFFFFF)
private fun Preview() = GridLauncherTheme {
    SettingsBottomSheet()
}