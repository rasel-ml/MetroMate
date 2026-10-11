package app.mrm.metromate.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import mrtbuddy.composeapp.generated.resources.Res
import mrtbuddy.composeapp.generated.resources.calculate
import mrtbuddy.composeapp.generated.resources.card
import mrtbuddy.composeapp.generated.resources.history
import org.jetbrains.compose.resources.painterResource

@Composable
public fun CalculatorIcon() {
    return Icon(
        painter = painterResource(Res.drawable.calculate),
        contentDescription = "Calculate",
    )
}

@Composable
public fun CardIcon() {
    return Icon(
        painter = painterResource(Res.drawable.card),
        contentDescription = "Calculate",
    )
}

@Composable
public fun SettingsIcon() {
    return Icon(
        imageVector = Icons.Filled.Settings,
        contentDescription = "Settings",
    )
}

@Composable
public fun HistoryIcon() {
    return Icon(
        painter = painterResource(Res.drawable.history),
        contentDescription = "Apps",
    )
}
