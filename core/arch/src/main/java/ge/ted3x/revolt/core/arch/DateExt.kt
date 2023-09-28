package ge.ted3x.revolt.core.arch

import android.content.Context
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val HOUR_FORMAT = "HH:mm"
private const val WEEK_DAY_WITH_HOUR_FORMAT = "EEEE 'at' HH:mm"
private const val DATE_FORMAT = "dd/MM/yyyy HH:mm"

fun Long.toDate(context: Context): String {
    val date = Date(this)
    val current = System.currentTimeMillis()
    val sdf: SimpleDateFormat
    DateUtils.isToday(current)
    return when {
        DateUtils.isToday(this) -> {
            sdf = SimpleDateFormat(HOUR_FORMAT, Locale.US)
            context.getString(R.string.date_today_at, sdf.format(date))
        }

        isYesterday(this) -> {
            sdf = SimpleDateFormat(HOUR_FORMAT, Locale.US)
            context.getString(R.string.date_yesterday_at, sdf.format(date))
        }

        (current - this) <= DateUtils.WEEK_IN_MILLIS -> {
            sdf = SimpleDateFormat(WEEK_DAY_WITH_HOUR_FORMAT, Locale.US)
            context.getString(R.string.date_last_day_at, sdf.format(date))
        }

        else -> {
            sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
            sdf.format(date)
        }
    }
}

fun isYesterday(millis: Long): Boolean {
    return DateUtils.isToday(millis + DateUtils.DAY_IN_MILLIS)
}

fun isLastWeek(millis: Long): Boolean {
    return DateUtils.isToday(millis + DateUtils.WEEK_IN_MILLIS)
}

fun Long.toHour(): String? {
    val date = Date(this)
    return SimpleDateFormat(HOUR_FORMAT, Locale.US).format(date)
}