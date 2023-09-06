package ski.mashiro.service

import ski.mashiro.entity.Course

interface MessageService {
    suspend fun getCourseMsg(course: Course): String
    suspend fun getCoursesMsg(courseList: List<Course>): String
    suspend fun getCoursesWithoutDayOfWeekMsg(courseList: List<Course>): String
}