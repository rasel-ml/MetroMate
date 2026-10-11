import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest
import mrtbuddy.composeapp.generated.resources.Res
import mrtbuddy.composeapp.generated.resources.autoSaveCardDetails
import mrtbuddy.composeapp.generated.resources.autoSaveCardDetailsDescription
import mrtbuddy.composeapp.generated.resources.dark_mode
import mrtbuddy.composeapp.generated.resources.dark_mode_config_dark
import mrtbuddy.composeapp.generated.resources.dark_mode_config_light
import mrtbuddy.composeapp.generated.resources.dark_mode_config_system_default
import mrtbuddy.composeapp.generated.resources.dark_mode_preference
import mrtbuddy.composeapp.generated.resources.language
import mrtbuddy.composeapp.generated.resources.settings
import app.mrm.metromate.Language
import app.mrm.metromate.settings.model.DarkThemeConfig
import app.mrm.metromate.ui.screens.more.MoreScreenAction
import app.mrm.metromate.ui.screens.more.MoreScreenEvent
import app.mrm.metromate.ui.screens.more.MoreScreenViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
    viewModel: MoreScreenViewModel = koinViewModel(),
) {
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(MoreScreenAction.OnInit)
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is MoreScreenEvent.Error -> {
                    // Handle error event (e.g., show a Toast or Snackbar)
                }
            }
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .then(modifier)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Column {
            SectionHeader(text = stringResource(Res.string.settings))
            RoundedButton(
                text = stringResource(Res.string.autoSaveCardDetails),
                subtitle = stringResource(Res.string.autoSaveCardDetailsDescription),
                onClick = { },
                trailing = {
                    Switch(
                        checked = uiState.autoSaveEnabled,
                        onCheckedChange = { enabled ->
                            viewModel.onAction(MoreScreenAction.SetAutoSave(enabled))
                        },
                    )
                },
            )

            RoundedButton(
                text = stringResource(Res.string.language),
                painter = painterResource(Res.drawable.language),
                onClick = {
                    if (uiState.currentLanguage == Language.English.isoFormat) {
                        viewModel.onAction(MoreScreenAction.SetLanguage(Language.Bangla.isoFormat))
                    } else {
                        viewModel.onAction(MoreScreenAction.SetLanguage(Language.English.isoFormat))
                    }
                },
                trailing = {
                    Text(
                        text = if (uiState.currentLanguage == Language.English.isoFormat) "English" else "বাংলা",
                        modifier = Modifier.padding(end = 8.dp),
                    )
                },
            )

            RoundedButton(
                text = stringResource(Res.string.dark_mode_preference),
                painter = painterResource(Res.drawable.dark_mode),
                onClick = {
                    val currentConfig = uiState.darkThemeConfig
                    val nextConfig =
                        when (currentConfig) {
                            DarkThemeConfig.FOLLOW_SYSTEM -> DarkThemeConfig.DARK
                            DarkThemeConfig.DARK -> DarkThemeConfig.LIGHT
                            DarkThemeConfig.LIGHT -> DarkThemeConfig.FOLLOW_SYSTEM
                        }
                    viewModel.onAction(MoreScreenAction.SetDarkThemeConfig(nextConfig))
                },
                trailing = {
                    Text(
                        text =
                            when (uiState.darkThemeConfig) {
                                DarkThemeConfig.FOLLOW_SYSTEM -> stringResource(Res.string.dark_mode_config_system_default)
                                DarkThemeConfig.DARK -> stringResource(Res.string.dark_mode_config_dark)
                                DarkThemeConfig.LIGHT -> stringResource(Res.string.dark_mode_config_light)
                            },
                        modifier = Modifier.padding(end = 8.dp),
                    )
                },
            )
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        modifier = Modifier.padding(vertical = 8.dp),
    )
}

@Composable
private fun RoundedButton(
    text: String,
    subtitle: String? = null,
    painter: Painter? = null,
    iconTint: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
    onClick: () -> Unit,
    trailing: @Composable (() -> Unit)? = null,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        onClick = onClick,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                if (painter != null) {
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        tint = iconTint,
                        modifier =
                            Modifier
                                .padding(end = 16.dp)
                                .size(24.dp),
                    )
                }
                Column {
                    Text(
                        text = text,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
            }
            trailing?.invoke()
        }
    }
}
