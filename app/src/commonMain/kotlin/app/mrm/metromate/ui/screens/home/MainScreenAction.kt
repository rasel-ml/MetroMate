package app.mrm.metromate.ui.screens.home

import app.mrm.metromate.model.CardReadResult
import app.mrm.metromate.model.CardState

sealed interface MainScreenAction {
    data object OnInit : MainScreenAction

    data class UpdateCardState(val newState: CardState) : MainScreenAction

    data class UpdateCardReadResult(val cardReadResult: CardReadResult) : MainScreenAction
}
