package ski.mashiro.data

import ski.mashiro.dto.CourseDTO
import ski.mashiro.util.JsonUtils
import ski.mashiro.util.TimeUtils
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

class CourseDataTest {

    @Test
    fun testFilter() {
        val json = "{\n" +
                "    \"code\": 20011,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"courseId\": 39,\n" +
                "            \"dayOfWeek\": \"Thursday\",\n" +
                "            \"time\": \"20:10 - 21:40\",\n" +
                "            \"name\": \"文献与信息检索\",\n" +
                "            \"place\": \"信息楼411\",\n" +
                "            \"teacher\": \"随婷婷\",\n" +
                "            \"week\": \"1 - 15\",\n" +
                "            \"oddWeek\": \"单\",\n" +
                "            \"credit\": 1.0\n" +
                "        }\n" +
                "    ],\n" +
                "    \"msg\": null\n" +
                "}"
        val result = JsonUtils.trans2ResultList(json, CourseDTO::class.java)
        val now = LocalDateTime.now()
        println(LinkedList(
            result.data!!
                .filter {
                    now.isBefore(TimeUtils.getCourseEndTime(now, it.time!!))
                }.sortedWith { o1, o2 ->
                    TimeUtils.compareStrTime(now, o1.time!!, o2.time!!)
                }
        ))
    }

}