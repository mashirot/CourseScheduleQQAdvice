package ski.mashiro.util

import kotlin.test.Test

class TimeUtilsTest {

    @Test
    fun testGetInitDelay() {
        println(TimeUtils.getInitDelay(1))
        println("==========")
        println(TimeUtils.getInitDelay(6))
    }

}