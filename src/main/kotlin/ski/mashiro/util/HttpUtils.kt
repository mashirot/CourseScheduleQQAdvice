package ski.mashiro.util

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ski.mashiro.CourseScheduleQQAdvice
import ski.mashiro.common.Result
import ski.mashiro.constant.Consts
import ski.mashiro.dto.ApiCourseSearchDTO
import ski.mashiro.dto.CourseDTO
import ski.mashiro.file.ConfigOp

object HttpUtils {

    private val logger = CourseScheduleQQAdvice.logger

    fun sendReq(apiCourseSearchDTO: ApiCourseSearchDTO): Result<List<CourseDTO>?> {
        val httpClint = OkHttpClient()
        val request = Request.Builder()
            .url(ConfigOp.config.apiAddress + "/api/sel")
            .addHeader("Content-Type", "application/json")
            .post(JsonUtils.trans2Json(apiCourseSearchDTO).toRequestBody())
            .build()
        return try {
            httpClint.newCall(request).execute().let {
                if (it.code != 200) {
                    return Result.failed(500, "API服务异常")
                }
                val json = it.body!!.string()
                val data = JsonUtils.trans2Obj(json, Result::class.java)
                when (data.code) {
                    Consts.USER_LOGIN_SUCCESS -> {
                        Result.success(Consts.USER_LOGIN_SUCCESS, null)
                    }
                    Consts.USER_LOGIN_FAILED -> {
                        Result.failed(Consts.USER_LOGIN_FAILED, "用户名或token错误")
                    }
                    Consts.COURSE_LIST_FAILED -> {
                        Result.failed(Consts.COURSE_LIST_FAILED, "获取课程失败")
                    }
                    Consts.COURSE_LIST_SUCCESS -> {
                        JsonUtils.trans2ResultList(json, CourseDTO::class.java)
                    }
                    else -> {
                        return Result.failed(500, "API服务异常")
                    }
                }
            }
        } catch (e: Exception) {
            logger.error(e.message)
            Result.failed(50000, "服务异常")
        }
    }

}