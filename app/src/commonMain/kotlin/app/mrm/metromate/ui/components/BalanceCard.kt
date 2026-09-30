package app.mrm.metromate.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import mrtbuddy.composeapp.generated.resources.Res
import mrtbuddy.composeapp.generated.resources.card
import mrtbuddy.composeapp.generated.resources.hold
import mrtbuddy.composeapp.generated.resources.keepCardSteady
import mrtbuddy.composeapp.generated.resources.latestBalance
import mrtbuddy.composeapp.generated.resources.lowBalance
import mrtbuddy.composeapp.generated.resources.noNfcSupport
import mrtbuddy.composeapp.generated.resources.readingCard
import mrtbuddy.composeapp.generated.resources.requiredNfc
import mrtbuddy.composeapp.generated.resources.rescan
import mrtbuddy.composeapp.generated.resources.tap
import mrtbuddy.composeapp.generated.resources.tapRescanToStart
import app.mrm.metromate.getPlatform
import app.mrm.metromate.managers.RescanManager
import app.mrm.metromate.model.CardState
import app.mrm.metromate.translateNumber
import app.mrm.metromate.ui.theme.Alert_yellow_D
import app.mrm.metromate.ui.theme.Alert_yellow_L
import app.mrm.metromate.ui.theme.DarkMRTPass
import app.mrm.metromate.ui.theme.DarkRapidPass
import app.mrm.metromate.ui.theme.LightMRTPass
import app.mrm.metromate.ui.theme.LightRapidPass
import app.mrm.metromate.utils.isRapidPassIdm
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BalanceCard(
    cardState: CardState,
    cardIdm: String? = null,
    cardName: String? = null,
    modifier: Modifier = Modifier,
) {
    val isDark = isSystemInDarkTheme()
    val isBalance = cardState is CardState.Balance
    val isRapidPass = cardIdm?.let { isRapidPassIdm(it) } ?: false
    val accent =
        if (isRapidPass) {
            if (isDark) DarkRapidPass else LightRapidPass
        } else {
            if (isDark) DarkMRTPass else LightMRTPass
        }
    val shape = RoundedCornerShape(28.dp)
    val background =
        if (isBalance) {
            Brush.linearGradient(listOf(accent, accent.copy(alpha = 0.72f).compositeOver(Color.Black)))
        } else {
            Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.surfaceContainer,
                    MaterialTheme.colorScheme.surfaceContainer,
                ),
            )
        }
    val contentColor = if (isBalance) Color.White else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = modifier.fillMaxWidth().height(232.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (isBalance) 6.dp else 1.dp),
        shape = shape,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Box(Modifier.fillMaxSize().background(background)) {
                if (isBalance) {
                    // Decorative rings, drawn (not bitmaps) so they cost nothing in size
                    Canvas(Modifier.fillMaxSize()) {
                        drawCircle(Color.White.copy(alpha = 0.08f), radius = size.minDimension * 0.75f, center = Offset(size.width * 0.95f, -size.height * 0.05f))
                        drawCircle(Color.White.copy(alpha = 0.06f), radius = size.minDimension * 0.5f, center = Offset(size.width * 0.05f, size.height * 1.05f))
                    }
                    if (!cardName.isNullOrBlank()) {
                        Text(
                            text = cardName,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier =
                                Modifier
                                    .align(Alignment.TopStart)
                                    .padding(20.dp)
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(50))
                                    .padding(horizontal = 14.dp, vertical = 6.dp),
                        )
                    }
                }

                if (getPlatform().name != "android") {
                    Text(
                        stringResource(Res.string.rescan),
                        modifier =
                            Modifier
                                .align(Alignment.TopEnd)
                                .padding(20.dp)
                                .clickable { RescanManager.requestRescan() },
                        style = MaterialTheme.typography.bodyLarge,
                        color = contentColor,
                    )
                }

                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    when (cardState) {
                        is CardState.Balance -> BalanceContent(amount = cardState.amount)
                        CardState.Reading -> ReadingContent()
                        CardState.WaitingForTap -> WaitingContent()
                        is CardState.Error -> ErrorContent(message = cardState.message)
                        CardState.NoNfcSupport -> NoNfcSupportContent()
                        CardState.NfcDisabled -> NfcDisabledContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun PulsingCircle(iconSize: Dp) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
    )
    val density = LocalDensity.current
    val initialRadiusPx = with(density) { iconSize.toPx() / 2 }
    val targetRadiusPx = initialRadiusPx * 2

    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = initialRadiusPx,
        targetValue = targetRadiusPx,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
    )

    // Retrieve the color outside the Canvas lambda
    val circleColor = MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha)

    Canvas(
        modifier = Modifier.size(iconSize * 2),
    ) {
        drawCircle(
            color = circleColor,
            radius = pulseRadius,
            center = center,
        )
    }
}

@Composable
private fun BalanceContent(amount: Int) {
    Text(
        text = stringResource(Res.string.latestBalance),
        style = MaterialTheme.typography.titleMedium,
        color = Color.White.copy(alpha = 0.8f),
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "৳ ${translateNumber(amount)}",
        style =
            MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
        color = Color.White,
    )
    if (amount <= 70) {
        Spacer(modifier = Modifier.height(12.dp))
        val chipColor =
            when {
                amount <= 20 -> Color(0xFFFF5252)
                amount <= 50 -> Color(0xFFFFAB40)
                else -> if (isSystemInDarkTheme()) Alert_yellow_D else Alert_yellow_L
            }
        Text(
            text = if (amount <= 20) stringResource(Res.string.lowBalance) else "●",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .background(chipColor, RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun ReadingContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Reading",
            modifier = Modifier.height(48.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.readingCard),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.keepCardSteady),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
    }
}

@Composable
private fun WaitingContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (getPlatform().name == "android") {
                PulsingCircle(iconSize = 48.dp)
            }
            Icon(
                painter = painterResource(Res.drawable.card),
                contentDescription = "Tap Card",
                modifier = Modifier.height(48.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(Res.string.tap),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (getPlatform().name != "android") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.tapRescanToStart),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        } else {
            Text(
                text = stringResource(Res.string.hold),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
private fun ErrorContent(message: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Error",
            modifier = Modifier.height(48.dp),
            tint = MaterialTheme.colorScheme.error,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Error",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
        if (getPlatform().name != "android") {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.rescan),
                modifier = Modifier.clickable { RescanManager.requestRescan() },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun NoNfcSupportContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "No NFC",
            modifier = Modifier.height(48.dp),
            tint = MaterialTheme.colorScheme.error,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.noNfcSupport),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.requiredNfc),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
    }
}

@Composable
internal expect fun NfcDisabledContent()
