package com.danielvilha.fulora.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

private const val DARK_MODE_PREVIEW_NAME = "Dark Mode"
private const val LIGHT_MODE_PREVIEW_NAME = "Light Mode"

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    name = LIGHT_MODE_PREVIEW_NAME,
    device = Devices.PIXEL_9_PRO,
    apiLevel = 36
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    name = DARK_MODE_PREVIEW_NAME,
    device = Devices.PIXEL_9_PRO,
    apiLevel = 36
)

@ExcludeFromJacocoGeneratedReport
annotation class LightDarkPreview()
