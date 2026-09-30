package app.mrm.metromate.ui.screens.farecalculator

import app.mrm.metromate.data.model.Station
import app.mrm.metromate.model.CardState

data class FareCalculatorState(
    val cardState: CardState = CardState.WaitingForTap,
    val fromStation: Station? = null,
    val toStation: Station? = null,
    val calculatedFare: Int = 0,
    val discountedFare: Int = 0,
    val fromExpanded: Boolean = false,
    val toExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
