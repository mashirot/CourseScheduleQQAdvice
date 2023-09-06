package ski.mashiro.service.impl

import ski.mashiro.entity.Course
import ski.mashiro.service.MessageService
import ski.mashiro.util.TimeUtils

object MessageServiceImpl : MessageService {

    private const val TITLE = "日期\t\t\t上课时间\t\t\t上课地点\t\t\t课程名"
    private const val WITHOUT_DAY_OF_WEEK_TITLE = "上课时间\t\t\t上课地点\t\t\t课程名"
    private const val NO_COURSE = "暂无课程"
    override suspend fun getCourseMsg(course: Course): String =
        StringBuilder()
            .append(TimeUtils.getToday()).append("\t")
            .append(TimeUtils.getDayOfWeek()).append("\n")
            .append(WITHOUT_DAY_OF_WEEK_TITLE).append("\n")
            .append(formatCourseWithoutDayOfWeek(course))
            .toString()

    override suspend fun getCoursesMsg(courseList: List<Course>) =
        StringBuilder().let { sb ->
            sb.append(TimeUtils.getToday()).append("\t")
                .append(TimeUtils.getDayOfWeek()).append("\n")
            if (courseList.isEmpty()) {
                sb.append(NO_COURSE)
                return@let sb.trimEnd().toString()
            }
            sb.append(TITLE).append("\n")
            courseList.forEach {
                sb.append(formatCourse(it)).append("\n")
            }
            sb.trimEnd().toString()
        }


    override suspend fun getCoursesWithoutDayOfWeekMsg(courseList: List<Course>) =
        StringBuilder().let { sb ->
            sb.append(TimeUtils.getToday()).append("\t")
                .append(TimeUtils.getDayOfWeek()).append("\n")
            if (courseList.isEmpty()) {
                sb.append(NO_COURSE)
                return@let sb.trimEnd().toString()
            }
            sb.append(WITHOUT_DAY_OF_WEEK_TITLE).append("\n")
            courseList.forEach {
                sb.append(formatCourseWithoutDayOfWeek(it)).append("\n")
            }
            sb.trimEnd().toString()
        }

    private fun formatCourse(course: Course): String {
        return "${course.dayOfWeek}\t\t${course.time}\t\t${course.place}\t\t${course.name}"
    }

    private fun formatCourseWithoutDayOfWeek(course: Course): String {
        return "${course.time}\t\t${course.place}\t\t${course.name}"
    }
}