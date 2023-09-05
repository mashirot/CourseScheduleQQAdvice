package ski.mashiro.util

import ski.mashiro.dto.CourseDTO
import kotlin.test.Test

class JsonUtilsTest {
    @Test
    fun testTrans2ResultList() {
        val json = "{\n" +
                "    \"code\": 20011,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"courseId\": 24,\n" +
                "            \"dayOfWeek\": \"Monday\",\n" +
                "            \"time\": \"17:30 - 19:05\",\n" +
                "            \"name\": \"大学美育\",\n" +
                "            \"place\": \"B教201\",\n" +
                "            \"teacher\": \"董雪静\",\n" +
                "            \"week\": \"1 - 16\",\n" +
                "            \"oddWeek\": \"-\",\n" +
                "            \"credit\": 2.0\n" +
                "        },\n" +
                "        {\n" +
                "            \"courseId\": 39,\n" +
                "            \"dayOfWeek\": \"Thursday\",\n" +
                "            \"time\": \"08:10 - 09:40\",\n" +
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
        val trans2ResultList = JsonUtils.trans2ResultList(json, CourseDTO::class.java)
        println(trans2ResultList)
    }
}