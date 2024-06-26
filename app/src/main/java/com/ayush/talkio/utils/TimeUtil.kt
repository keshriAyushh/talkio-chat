package com.ayush.talkio.utils

import java.util.Calendar
import java.util.Locale

class TimeUtil {

    companion object {
        private const val SECOND_MILLS = 1000
        private const val MINUTE_MILLS = 60 * SECOND_MILLS
        private const val HOUR_MILLS = 60 * MINUTE_MILLS
        private const val DAY_MILLS = 24 * HOUR_MILLS

        fun getTimeAgo(time: Long): String? {
            val now: Long = System.currentTimeMillis()

            if (time > now || time <= 0) {
                return null
            }

            val diff = now - time

            return if (diff < MINUTE_MILLS) {
                "just now"
            } else if (diff < 2 * MINUTE_MILLS) {
                "a minute ago"
            } else if (diff < 50 * MINUTE_MILLS) {
                (diff / MINUTE_MILLS).toString() + " minutes ago"
            } else if (diff < 90 * MINUTE_MILLS) {
                "an hour ago"
            } else if (diff < 24 * HOUR_MILLS) {
                (diff / HOUR_MILLS).toString() + " hours ago"
            } else if (diff < 48 * HOUR_MILLS) {
                "yesterday"
            } else {
                (diff / DAY_MILLS).toString() + " days ago"
            }
        }

        fun getShortDate(ts: Long?): String {
            if (ts == null) return ""
            //Get instance of calendar
            val calendar = Calendar.getInstance(Locale.getDefault())
            //get current date from ts
            calendar.timeInMillis = ts
            //return formatted date
            return android.text.format.DateFormat.format("E, dd MMM yyyy", calendar).toString()
        }

    }

}