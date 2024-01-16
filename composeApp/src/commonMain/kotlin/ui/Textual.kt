package ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
import model.CardUiModel
import kotlin.math.absoluteValue

@Composable
fun DueDateView(cardUiModel: CardUiModel) {
    Row(modifier = Modifier.fillMaxHeight()) {
        val tz = TimeZone.currentSystemDefault()

        val todate = Clock.System.now().toLocalDateTime(tz).date

        val dueDateInstant = Instant.fromEpochMilliseconds(cardUiModel.billDueDate)
        val dueDate = dueDateInstant.toLocalDateTime(tz).date
        val dueDateInDays = todate.until(dueDate, DateTimeUnit.DAY)

        val today = todate == dueDate

        val dueDateNumber = dueDate.dayOfMonth
        val dueDateName = dueDate.month.name.lowercase().substring(0, 3).replaceFirstChar {
            it.uppercase()
        }
        val humanReadableDate = "$dueDateNumber $dueDateName"

        val late = dueDateInDays < 0

        Text(modifier = Modifier.align(Alignment.Bottom), text = "$humanReadableDate")
        Spacer(modifier = Modifier.width(16.dp))

        val formattedDueDateReadable = if (dueDateInDays == 1) {
            "besok"
        } else if (today) {
            "hari ini"
        } else if (dueDateInDays < 0) {
            "lewat ${dueDateInDays.absoluteValue} hari"
        } else {
            "$dueDateInDays hari lagi"
        }
        if (late) {
            Row(
                modifier = Modifier.align(Alignment.Bottom),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.height(16.dp),
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = formattedDueDateReadable,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            Text(modifier = Modifier.align(Alignment.Bottom), text = formattedDueDateReadable)
        }
    }
}