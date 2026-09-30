package app.mrm.metromate

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.mrm.metromate.managers.RescanManager
import app.mrm.metromate.nfc.getNFCManager
import app.mrm.metromate.settings.model.DarkThemeConfig
import app.mrm.metromate.ui.screens.home.MainScreen
import app.mrm.metromate.ui.screens.home.MainScreenAction
import app.mrm.metromate.ui.screens.home.MainScreenEvent
import app.mrm.metromate.ui.screens.home.MainScreenState
import app.mrm.metromate.ui.screens.home.MainScreenViewModel
import app.mrm.metromate.ui.theme.MRTBuddyTheme
import app.mrm.metromate.utils.observeAsActions
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(dynamicColor: Boolean) {
    val mainVm = koinViewModel<MainScreenViewModel>()
    val nfcManager = getNFCManager()

    mainVm.events.observeAsActions { event ->
        when (event) {
            is MainScreenEvent.Error -> {}
            MainScreenEvent.ShowMessage -> {}
        }
    }

    if (RescanManager.isRescanRequested.value) {
        nfcManager.startScan()
        RescanManager.resetRescanRequest()
    }

    // Collect NFC state and card read results - runs once per composition lifecycle
    LaunchedEffect(nfcManager) {
        nfcManager.cardReadResults.collect { result ->
            result?.let {
                mainVm.onAction(MainScreenAction.UpdateCardReadResult(it))
            }
        }
    }

    LaunchedEffect(nfcManager) {
        nfcManager.cardState.collect {
            mainVm.onAction(MainScreenAction.UpdateCardState(it))
        }
    }

    // Start NFC scan once
    nfcManager.startScan()

    val state: MainScreenState by mainVm.state.collectAsState()

    MRTBuddyTheme(
        darkTheme =
            when (state.darkThemeConfig) {
                DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
                DarkThemeConfig.DARK -> true
                DarkThemeConfig.LIGHT -> false
            },
        dynamicColor = dynamicColor,
    ) {
        LocalizedApp(
            language = state.currentLanguage,
        ) {
            MainScreen()
        }
    }
}
