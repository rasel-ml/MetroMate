package app.mrm.metromate.ui.screens.farecalculator

import app.mrm.metromate.data.model.Station
import app.mrm.metromate.model.CardState

sealed interface FareCalculatorAction {
    object OnInit : FareCalculatorAction

    data class UpdateFromStation(val station: Station) : FareCalculatorAction

    data class UpdateToStation(val station: Station) : FareCalculatorAction

    data class UpdateCardState(val cardState: CardState) : FareCalculatorAction

    object ToggleFromExpanded : FareCalculatorAction

    object ToggleToExpanded : FareCalculatorAction

    object DismissDropdowns : FareCalculatorAction
}
