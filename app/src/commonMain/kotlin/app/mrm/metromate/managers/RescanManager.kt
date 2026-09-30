package app.mrm.metromate.managers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import app.mrm.metromate.getPlatform

object RescanManager {
    private val _isRescanRequested = mutableStateOf(false)
    val isRescanRequested: MutableState<Boolean> = _isRescanRequested

    fun requestRescan() {
        if (getPlatform().name != "android") {
            _isRescanRequested.value = true
        }
    }

    fun resetRescanRequest() {
        _isRescanRequested.value = false
    }
}
