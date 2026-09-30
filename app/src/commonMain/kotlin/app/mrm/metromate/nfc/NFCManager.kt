package app.mrm.metromate.nfc

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.SharedFlow
import app.mrm.metromate.model.CardReadResult
import app.mrm.metromate.model.CardState

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class NFCManager() {
    val cardState: SharedFlow<CardState>
    val cardReadResults: SharedFlow<CardReadResult?>

    @Composable
    fun startScan()

    fun stopScan()

    fun isEnabled(): Boolean

    fun isSupported(): Boolean
}

@Composable
expect fun getNFCManager(): NFCManager
