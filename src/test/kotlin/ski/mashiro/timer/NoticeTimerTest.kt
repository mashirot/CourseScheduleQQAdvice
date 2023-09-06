package ski.mashiro.timer

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.time.Duration

class NoticeTimerTest {

    @Test
    fun testDurationParse() {
        println(Duration.parse("1d").inWholeHours)
    }

    @Test
    fun testLocalDateTimeOf() {
        val now = LocalDateTime.now()
        val time = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 6, 0)
        println(now)
        println(time)
        println(time.second)
    }
}