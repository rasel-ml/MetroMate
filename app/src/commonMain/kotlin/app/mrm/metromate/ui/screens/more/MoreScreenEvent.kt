package app.mrm.metromate.ui.screens.more

sealed interface MoreScreenEvent {
    data class Error(val message: String) : MoreScreenEvent

    object NavigateTooStationMap : MoreScreenEvent

    object NavigateToLicenses : MoreScreenEvent
}
