package ski.mashiro.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object TimeUtils {

    fun getDayOfWeek(): String {
        return WEEK_MAP[LocalDate.now().dayOfWeek.value]!!
    }

    fun getToday(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    private val WEEK_MAP = mapOf(
        1 to "Monday",
        2 to "Tuesday",
        3 to "Wednesday",
        4 to "Thursday",
        5 to "Friday",
        6 to "Saturday",
        7 to "Sunday",
    )
}