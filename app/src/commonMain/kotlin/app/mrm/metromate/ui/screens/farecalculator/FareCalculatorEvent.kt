package app.mrm.metromate.ui.screens.farecalculator

sealed interface FareCalculatorEvent {
    data class Error(val message: String) : FareCalculatorEvent
}
