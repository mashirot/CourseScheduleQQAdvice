package ski.mashiro.service

import net.mamoe.mirai.console.command.CommandSender
import ski.mashiro.common.Result
import ski.mashiro.entity.Course

interface MessageService {
    suspend fun sendCourses(sender: CommandSender, result: Result<List<Course>?>)
    suspend fun sendCoursesWithoutDayOfWeek(sender: CommandSender, result: Result<List<Course>?>)
}