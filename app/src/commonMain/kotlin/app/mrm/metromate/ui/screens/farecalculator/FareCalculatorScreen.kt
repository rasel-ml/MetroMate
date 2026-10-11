package app.mrm.metromate.ui.screens.farecalculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import mrtbuddy.composeapp.generated.resources.Res
import mrtbuddy.composeapp.generated.resources.station_map
import mrtbuddy.composeapp.generated.resources.stationMap
import app.mrm.metromate.model.CardState
import app.mrm.metromate.ui.screens.components.FareDisplayCard
import app.mrm.metromate.ui.screens.components.StationSelectionSection
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FareCalculatorScreen(
    modifier: Modifier = Modifier,
    viewModel: FareCalculatorViewModel = koinViewModel(),
    cardState: CardState,
    onNavigateToStationMap: () -> Unit = {},
) {
    val uiState = viewModel.state.collectAsState()

    // Update card state when it changes
    LaunchedEffect(cardState) {
        viewModel.onAction(FareCalculatorAction.UpdateCardState(cardState))
    }

    // Initialize ViewModel
    LaunchedEffect(Unit) {
        viewModel.onAction(FareCalculatorAction.OnInit)
    }

    // Handle events from ViewModel
    LaunchedEffect(viewModel.events) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is FareCalculatorEvent.Error -> {
                    // Handle error event
                }
            }
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FareDisplayCard(uiState.value, viewModel)
        Spacer(modifier = Modifier.height(4.dp))
        StationSelectionSection(uiState.value, viewModel)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onNavigateToStationMap,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(),
        ) {
            Icon(
                painter = painterResource(Res.drawable.station_map),
                contentDescription = null,
                modifier = Modifier.height(20.dp),
            )
            Text(
                text = stringResource(Res.string.stationMap),
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}
