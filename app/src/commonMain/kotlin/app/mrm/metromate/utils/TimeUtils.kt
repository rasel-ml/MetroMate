package app.mrm.metromate.utils

import androidx.compose.runtime.Composable
import app.mrm.metromate.generated.resources.Res
import app.mrm.metromate.generated.resources.dayAgo
import app.mrm.metromate.generated.resources.daysAgo
import app.mrm.metromate.generated.resources.hourAgo
import app.mrm.metromate.generated.resources.hoursAgo
import app.mrm.metromate.generated.resources.justNow
import app.mrm.metromate.generated.resources.minuteAgo
import app.mrm.metromate.generated.resources.minutesAgo
import app.mrm.metromate.generated.resources.monthAgo
import app.mrm.metromate.generated.resources.monthsAgo
import app.mrm.metromate.generated.resources.yearAgo
import app.mrm.metromate.generated.resources.yearsAgo
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.stringResource

object TimeUtils {
    @Composable
    fun getTimeAgoString(timestamp: Long): String {
        val now = Clock.System.now().toEpochMilliseconds()
        val diff = now - timestamp

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30
        val years = days / 365

        return when {
            years > 0 -> "$years ${stringResource(if (years == 1L) Res.string.yearAgo else Res.string.yearsAgo)}"
            months > 0 -> "$months ${stringResource(if (months == 1L) Res.string.monthAgo else Res.string.monthsAgo)}"
            days > 0 -> "$days ${stringResource(if (days == 1L) Res.string.dayAgo else Res.string.daysAgo)}"
            hours > 0 -> "$hours ${stringResource(if (hours == 1L) Res.string.hourAgo else Res.string.hoursAgo)}"
            minutes > 0 -> "$minutes ${stringResource(if (minutes == 1L) Res.string.minuteAgo else Res.string.minutesAgo)}"
            else -> stringResource(Res.string.justNow)
        }
    }
}
