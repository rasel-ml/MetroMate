package app.mrm.metromate.ui.screens.history

import app.mrm.metromate.data.CardEntity

data class HistoryScreenState(
    val isLoading: Boolean = false,
    val cards: List<CardWithBalance> = emptyList(),
    val error: String? = null,
)

data class CardWithBalance(
    val card: CardEntity,
    val balance: Int?,
)
