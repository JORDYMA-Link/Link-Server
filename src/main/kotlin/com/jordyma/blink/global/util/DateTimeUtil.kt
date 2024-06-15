package com.jordyma.blink.global.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

object DateTimeUtils {
    fun convertToLocalDateTime(date: Date): LocalDateTime {
        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    fun stringToLocalDate(localDate: String): LocalDate {
        return LocalDate.parse(localDate, DateTimeFormatter.ISO_DATE)
    }

    fun localDateToString(localDate: LocalDate): String {
        return localDate.format(DateTimeFormatter.ISO_DATE)
    }

    fun convertDayOfWeekToKor(dayOfWeek: DayOfWeek): String {
        return dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN)
    }

    fun convertDayOfWeekToEng(dayOfWeek: DayOfWeek): String {
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US)
    }
}