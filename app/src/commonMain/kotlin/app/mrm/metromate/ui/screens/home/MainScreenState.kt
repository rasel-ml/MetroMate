package app.mrm.metromate.ui.screens.home

import app.mrm.metromate.model.CardState
import app.mrm.metromate.model.Transaction
import app.mrm.metromate.model.TransactionWithAmount
import app.mrm.metromate.settings.model.DarkThemeConfig

data class MainScreenState(
    val isLoading: Boolean = false,
    val cardState: CardState = CardState.WaitingForTap,
    val cardIdm: String? = null,
    val cardName: String? = null,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val transaction: List<Transaction> = emptyList(),
    val transactionWithAmount: List<TransactionWithAmount> = emptyList(),
    val error: String? = null,
    val currentLanguage: String = "en",
)
