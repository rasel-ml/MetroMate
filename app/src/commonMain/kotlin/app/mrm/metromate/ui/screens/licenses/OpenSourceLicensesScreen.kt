package app.mrm.metromate.ui.screens.licenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mrtbuddy.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun OpenSourceLicensesScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    paddingValues: PaddingValues,
) {
    Column(modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Open Source Licenses") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
            windowInsets = WindowInsets.statusBars,
        )
        var content by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            content =
                runCatching { Res.readBytes("files/open-source-licenses.md").decodeToString() }
                    .getOrDefault("")
                    .lines()
                    .joinToString("\n") { it.trimStart('#', ' ').replace("**", "").replace("`", "") }
        }

        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .padding(bottom = paddingValues.calculateBottomPadding()),
        )
    }
}
