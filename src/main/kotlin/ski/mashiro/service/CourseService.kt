package ski.mashiro.service

import ski.mashiro.common.Result
import ski.mashiro.entity.Course
import ski.mashiro.entity.User

interface CourseService {
    fun getTodayCoursesFromServer(user: User): Result<List<Course>?>
    fun getEffCoursesFromServer(user: User): Result<List<Course>?>
    fun getAllCoursesFromServer(user: User): Result<List<Course>?>
}