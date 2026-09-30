package app.mrm.metromate.ui.screens.more

import app.mrm.metromate.settings.model.DarkThemeConfig

sealed interface MoreScreenAction {
    object OnInit : MoreScreenAction

    data class SetAutoSave(val enabled: Boolean) : MoreScreenAction

    data class SetLanguage(val language: String) : MoreScreenAction

    data class SetDarkThemeConfig(val darkThemeConfig: DarkThemeConfig) : MoreScreenAction

    object StationMap : MoreScreenAction

    object OpenLicenses : MoreScreenAction
}
