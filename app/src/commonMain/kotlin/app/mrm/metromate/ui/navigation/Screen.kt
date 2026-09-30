package app.mrm.metromate.ui.navigation

/**
 * Represents the different screens in the application
 */
sealed class Screen {
    object CardScan : Screen()

    object FareCalculator : Screen()
}
