package utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Return formatted date: DD MMM
 *
 * Sample:
 *
 * - 02 May
 * - 13 Aug
 */
fun Long.toDdMonth(): String {
    val tz = TimeZone.currentSystemDefault()
    val dueDateInstant = Instant.fromEpochMilliseconds(this)
    val dueDate = dueDateInstant.toLocalDateTime(tz).date
    val dueDateNumber = dueDate.dayOfMonth
    val dueDateName = dueDate.month.name.lowercase().substring(0, 3).replaceFirstChar {
        it.uppercase()
    }

    return "${dueDateNumber.formatMinimumTwoDigit()} $dueDateName"
}

fun Int.formatMinimumTwoDigit(): String{
    return if(this in 0..9) "0$this" else this.toString()
}