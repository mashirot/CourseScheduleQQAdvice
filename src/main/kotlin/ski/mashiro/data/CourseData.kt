package ski.mashiro.data

import ski.mashiro.CourseScheduleQQAdvice
import ski.mashiro.constant.Consts
import ski.mashiro.entity.Course
import ski.mashiro.entity.User
import ski.mashiro.service.impl.CourseServiceImpl
import ski.mashiro.util.TimeUtils
import java.time.LocalDateTime
import java.util.*

object CourseData {

    private val logger = CourseScheduleQQAdvice.logger

    val courseMap = HashMap<Long, Queue<Course>>(10)

    fun getCourseData(now: LocalDateTime, user: User) {
        val result = CourseServiceImpl.getTodayCoursesFromServer(user)
        if (result.code != Consts.COURSE_LIST_SUCCESS) {
            logger.warning("用户: ${user.username} 获取今日课程失败")
            return
        }
        courseMap[user.qq] = LinkedList(
            result.data!!
                .filter {
                    now.isAfter(TimeUtils.getCourseEndTime(now, it.time))
                }.sortedWith { o1, o2 ->
                    TimeUtils.compareStrTime(now, o1.time, o2.time)
                }
        )
    }

}