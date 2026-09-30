package app.mrm.metromate.ui.screens.more

import app.mrm.metromate.settings.model.DarkThemeConfig

data class MoreScreenState(
    val isLoading: Boolean = false,
    val autoSaveEnabled: Boolean = false,
    val currentLanguage: String = "en",
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val error: String? = null,
)
