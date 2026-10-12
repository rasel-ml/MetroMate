package app.mrm.metromate.nfc.service

import androidx.compose.runtime.Composable
import app.mrm.metromate.generated.resources.Res
import app.mrm.metromate.generated.resources.agargaon
import app.mrm.metromate.generated.resources.bangladeshSecretariat
import app.mrm.metromate.generated.resources.bijoySarani
import app.mrm.metromate.generated.resources.dhakaUniversity
import app.mrm.metromate.generated.resources.farmgate
import app.mrm.metromate.generated.resources.kamalapur
import app.mrm.metromate.generated.resources.karwanBazar
import app.mrm.metromate.generated.resources.kazipara
import app.mrm.metromate.generated.resources.mirpur10
import app.mrm.metromate.generated.resources.mirpur11
import app.mrm.metromate.generated.resources.motijheel
import app.mrm.metromate.generated.resources.pallabi
import app.mrm.metromate.generated.resources.shahbagh
import app.mrm.metromate.generated.resources.shewrapara
import app.mrm.metromate.generated.resources.uttaraCenter
import app.mrm.metromate.generated.resources.uttaraNorth
import app.mrm.metromate.generated.resources.uttaraSouth
import org.jetbrains.compose.resources.stringResource

object StationService {
    private val stationMap =
        mapOf(
            10 to "Motijheel",
            20 to "Bangladesh Secretariat",
            25 to "Dhaka University",
            30 to "Shahbagh",
            35 to "Karwan Bazar",
            40 to "Farmgate",
            45 to "Bijoy Sarani",
            50 to "Agargaon",
            55 to "Shewrapara",
            60 to "Kazipara",
            65 to "Mirpur 10",
            70 to "Mirpur 11",
            75 to "Pallabi",
            80 to "Uttara South",
            85 to "Uttara Center",
            90 to "Uttara North",
            // Hatirjheel Bus Stations
            13 to "Mohanagar (HJ)",
            16 to "Rampura (HJ)",
            17 to "Badda (HJ)",
            19 to "Police Plaza (HJ)",
            28 to "FDC (HJ)",
            // ?? to "Modhubag (HJ)"
            // ?? to "Bou bazar (HJ)",
            // ?? to "Kunipara (HJ)",
        )

    fun getStationName(code: Int): String = stationMap.getOrElse(code) { "Unknown ($code)" }

    @Composable
    fun translate(stationName: String): String {
        return when (stationName) {
            "Kamalapur" -> stringResource(Res.string.kamalapur)
            "Motijheel" -> stringResource(Res.string.motijheel)
            "Bangladesh Secretariat" -> stringResource(Res.string.bangladeshSecretariat)
            "Dhaka University" -> stringResource(Res.string.dhakaUniversity)
            "Shahbagh" -> stringResource(Res.string.shahbagh)
            "Karwan Bazar" -> stringResource(Res.string.karwanBazar)
            "Farmgate" -> stringResource(Res.string.farmgate)
            "Bijoy Sarani" -> stringResource(Res.string.bijoySarani)
            "Agargaon" -> stringResource(Res.string.agargaon)
            "Shewrapara" -> stringResource(Res.string.shewrapara)
            "Kazipara" -> stringResource(Res.string.kazipara)
            "Mirpur 10" -> stringResource(Res.string.mirpur10)
            "Mirpur-10" -> stringResource(Res.string.mirpur10)
            "Mirpur 11" -> stringResource(Res.string.mirpur11)
            "Mirpur-11" -> stringResource(Res.string.mirpur11)
            "Pallabi" -> stringResource(Res.string.pallabi)
            "Uttara South" -> stringResource(Res.string.uttaraSouth)
            "Uttara Center" -> stringResource(Res.string.uttaraCenter)
            "Uttara North" -> stringResource(Res.string.uttaraNorth)
            else -> stationName
        }
    }
}
