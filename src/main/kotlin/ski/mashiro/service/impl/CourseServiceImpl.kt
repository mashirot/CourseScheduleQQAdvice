package ski.mashiro.service.impl

import ski.mashiro.common.Result
import ski.mashiro.constant.Consts
import ski.mashiro.dto.ApiCourseSearchDTO
import ski.mashiro.dto.CourseDTO
import ski.mashiro.entity.Course
import ski.mashiro.entity.User
import ski.mashiro.service.CourseService
import ski.mashiro.util.HttpUtils
import ski.mashiro.util.TimeUtils.getDayOfWeek

object CourseServiceImpl : CourseService {
    override fun getTodayCoursesFromServer(user: User): Result<List<Course>?> {
        val searchDTO =
            ApiCourseSearchDTO(user.username, user.apiToken, getDayOfWeek(), true)
        return trans2Courses(HttpUtils.sendReq(searchDTO))
    }

    override fun getEffCoursesFromServer(user: User): Result<List<Course>?> {
        val searchDTO = ApiCourseSearchDTO(user.username, user.apiToken, true)
        return trans2Courses(HttpUtils.sendReq(searchDTO))
    }

    override fun getAllCoursesFromServer(user: User): Result<List<Course>?> {
        val searchDTO = ApiCourseSearchDTO(user.username, user.apiToken, false)
        return trans2Courses(HttpUtils.sendReq(searchDTO))
    }

    private fun trans2Courses(result: Result<List<CourseDTO>?>): Result<List<Course>?> {
        return if (result.code == Consts.COURSE_LIST_SUCCESS) {
            Result.success(Consts.COURSE_LIST_SUCCESS, result.data!!.map {
                Course(it.dayOfWeek!!, it.time!!, it.name!!, it.place!!)
            })
        } else {
            Result.failed(result.code, result.msg)
        }
    }
}