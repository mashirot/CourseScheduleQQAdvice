package ski.mashiro.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object TimeUtils {

    private val WEEK_MAP = mapOf(
        1 to "Monday",
        2 to "Tuesday",
        3 to "Wednesday",
        4 to "Thursday",
        5 to "Friday",
        6 to "Saturday",
        7 to "Sunday",
    )

    fun getDayOfWeek(): String {
        return WEEK_MAP[LocalDate.now().dayOfWeek.value]!!
    }

    fun getToday(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    fun getInitDelay(hourOfDay: Int): Long {
        val startTime = LocalDateTime.now()
        val firstTime = LocalDateTime.of(startTime.year, startTime.month, startTime.dayOfMonth, hourOfDay, 0)
        return if (startTime.hour < hourOfDay) {
            firstTime.toInstant(ZoneOffset.UTC).toEpochMilli() - startTime.toInstant(ZoneOffset.UTC).toEpochMilli()
        } else {
            firstTime.plusDays(1).toInstant(ZoneOffset.UTC).toEpochMilli() - startTime.toInstant(ZoneOffset.UTC).toEpochMilli()
        }
    }

    /**
     * @param preTime hh:mm - hh:mm
     * @param nextTime hh:mm - hh:mm
     */
    fun compareStrTime(now: LocalDateTime, preTime: String, nextTime: String): Int =
        if (getCourseStartTime(now, preTime).isAfter(getCourseStartTime(now, nextTime))) 1 else -1

    /**
     * @param strTime hh:mm - hh:mm
     */
    fun getCourseStartTime(now: LocalDateTime, strTime: String): LocalDateTime {
        val split = strTime.split(" - ")[0].split(":")
        return LocalDateTime.of(now.year, now.month, now.dayOfMonth, split[0].toInt(), split[1].toInt())
    }

    /**
     * @param strTime hh:mm - hh:mm
     */
    fun getCourseEndTime(now: LocalDateTime, strTime: String): LocalDateTime {
        val split = strTime.split(" - ")[1].split(":")
        return LocalDateTime.of(now.year, now.month, now.dayOfMonth, split[0].toInt(), split[1].toInt())
    }
}