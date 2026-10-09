package com.resukisu.resukisu.ui.screen.themeSettings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.resukisu.resukisu.R
import com.resukisu.resukisu.ui.component.settings.SettingsChooseDialog
import com.resukisu.resukisu.ui.theme.ThemeConfig
import com.resukisu.resukisu.ui.viewmodel.SettingsUiAction
import com.resukisu.resukisu.ui.viewmodel.SettingsUiState
import com.resukisu.resukisu.ui.viewmodel.SettingsViewModel
import org.koin.compose.koinInject
import android.graphics.Color as AndroidColor


@Composable
fun ThemeSettingsDialogs(
    state: SettingsUiState,
    viewModel: SettingsViewModel
) {
    val themeConfig: ThemeConfig = koinInject()
    if (state.showThemeColorDialog) {
        ThemeColorDialog(
            currentSeedColor = themeConfig.seedColor,
            onColorSelected = { seedColor ->
                viewModel.dispatch(SettingsUiAction.SetThemeColor(seedColor))
                viewModel.dispatch(SettingsUiAction.SetThemeColorDialogVisible(false))
            },
            onDismiss = {
                viewModel.dispatch(SettingsUiAction.SetThemeColorDialogVisible(false))
            }
        )
    }
}

@Composable
fun LanguageSelectionDialog(
    currentLocale: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languageUseSystemDefault = stringResource(R.string.language_system_default)
    val customLanguageName = stringResource(R.string.language_mcnyyds)
    val languageTags = remember {
        listOf(
            "en-US",
            "ar",
            "az",
            "zh-XA",
            "be",
            "bn",
            "bn-BD",
            "bs",
            "da",
            "de",
            "es",
            "et",
            "fa",
            "fil",
            "fr",
            "gl",
            "hi",
            "hr",
            "hu",
            "in",
            "it",
            "iw",
            "ja",
            "kn",
            "ko",
            "lt",
            "lv",
            "mr",
            "ms",
            "nl",
            "pl",
            "pt",
            "pt-BR",
            "ro",
            "ru",
            "sl",
            "sr",
            "te",
            "th",
            "tk",
            "tr",
            "uk",
            "vi",
            "zh-CN",
            "zh-HK",
            "zh-TW"
        )
    }
    val allOptions = listOf("system" to languageUseSystemDefault) + languageTags.map { tag ->
        val locale = java.util.Locale.forLanguageTag(tag)
        locale.toLanguageTag() to if (locale.toLanguageTag().equals("zh-XA", ignoreCase = true)) {
            customLanguageName
        } else {
            locale.getDisplayName(locale)
        }
    }.sortedBy { it.second }
    var selectedIndex by remember(currentLocale, allOptions) {
        mutableIntStateOf(allOptions.indexOfFirst { (tag, _) ->
            currentLocale.replace('_', '-').equals(tag, ignoreCase = true)
        })
    }
    SettingsChooseDialog(
        show = true,
        title = stringResource(R.string.settings_language),
        items = allOptions.map { it.second },
        selectedIndex = selectedIndex,
        onDismiss = onDismiss,
        onSelectedIndexChange = { index ->
            selectedIndex = index
            allOptions.getOrNull(index)?.let { onLanguageSelected(it.first) }
            onDismiss()
        }
    )
}

@Composable
fun ThemeColorDialog(
    currentSeedColor: Int,
    onColorSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val initialHsv = remember(currentSeedColor) {
        FloatArray(3).also { AndroidColor.colorToHSV(currentSeedColor, it) }
    }
    var hue by remember(currentSeedColor) { mutableFloatStateOf(initialHsv[0]) }
    var saturation by remember(currentSeedColor) { mutableFloatStateOf(initialHsv[1]) }
    var value by remember(currentSeedColor) { mutableFloatStateOf(initialHsv[2]) }
    val selectedColor = AndroidColor.HSVToColor(floatArrayOf(hue, saturation, value))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.choose_theme_color)) },
        text = {
            ColorPicker(
                color = Color(selectedColor),
                hue = hue,
                saturation = saturation,
                value = value,
                onHueChange = { hue = it },
                onSaturationChange = { saturation = it },
                onValueChange = { value = it },
            )
        },
        confirmButton = {
            Button(onClick = { onColorSelected(selectedColor) }) {
                Icon(
                    Icons.TwoTone.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun ColorPicker(
    color: Color,
    hue: Float,
    saturation: Float,
    value: Float,
    onHueChange: (Float) -> Unit,
    onSaturationChange: (Float) -> Unit,
    onValueChange: (Float) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = color.toHexString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        ColorSlider(
            label = "H",
            value = hue,
            valueRange = 0f..360f,
            onValueChange = onHueChange
        )
        ColorSlider(
            label = "S",
            value = saturation,
            valueRange = 0f..1f,
            onValueChange = onSaturationChange
        )
        ColorSlider(
            label = "V",
            value = value,
            valueRange = 0f..1f,
            onValueChange = onValueChange
        )
    }
}

@Composable
private fun ColorSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(end = 12.dp)
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier.padding(start = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text( // Some stupid way to solve measure problem
                text = "360.00",
                style = MaterialTheme.typography.labelMediumEmphasized.copy(
                    fontFeatureSettings = "tnum"
                ),
                modifier = Modifier.alpha(0f)
            )
            Text(
                text = "%.2f".format(value),
                style = MaterialTheme.typography.labelMediumEmphasized.copy(
                    fontFeatureSettings = "tnum"
                ),
            )
        }
    }
}

private fun Color.toHexString(): String {
    val argb = AndroidColor.rgb(
        (red * 255).toInt().coerceIn(0, 255),
        (green * 255).toInt().coerceIn(0, 255),
        (blue * 255).toInt().coerceIn(0, 255)
    )
    return "#%06X".format(argb and 0x00FFFFFF)
}
