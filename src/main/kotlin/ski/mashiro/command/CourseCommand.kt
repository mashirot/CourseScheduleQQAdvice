package ski.mashiro.command

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.console.command.isUser
import ski.mashiro.CourseScheduleQQAdvice
import ski.mashiro.data.UserData
import ski.mashiro.file.ConfigOp
import ski.mashiro.service.impl.CourseServiceImpl
import ski.mashiro.service.impl.MessageServiceImpl

object CourseCommand : CompositeCommand(
    CourseScheduleQQAdvice, "course", "c"
) {
    @ExperimentalCommandDescriptors
    override val prefixOptional: Boolean
        get() = true

    @SubCommand("today", "t")
    suspend fun today(sender: CommandSender) {
        if (hasNoPerm(sender)) {
            return
        }
        if (isUnbind(sender)) {
            sender.sendMessage("请先绑定用户")
            return
        }
        val senderQQ = sender.user!!.id
        MessageServiceImpl.sendCourses(sender, CourseServiceImpl.getTodayCoursesFromServer(UserData.userMap[senderQQ]!!))
    }

    @SubCommand("eff", "eff")
    suspend fun effCourse(sender: CommandSender) {
        if (hasNoPerm(sender)) {
            return
        }
        if (isUnbind(sender)) {
            sender.sendMessage("请先绑定用户")
            return
        }
        val senderQQ = sender.user!!.id
        MessageServiceImpl.sendCourses(sender, CourseServiceImpl.getEffCoursesFromServer(UserData.userMap[senderQQ]!!))
    }

    @SubCommand("all")
    suspend fun allCourse(sender: CommandSender) {
        if (hasNoPerm(sender)) {
            return
        }
        if (isUnbind(sender)) {
            sender.sendMessage("请先绑定用户")
            return
        }
        val senderQQ = sender.user!!.id
        MessageServiceImpl.sendCourses(sender, CourseServiceImpl.getAllCoursesFromServer(UserData.userMap[senderQQ]!!))
    }

    private fun hasNoPerm(sender: CommandSender): Boolean {
        return sender.isConsole() ||
                sender.isUser() &&
                ConfigOp.config.ownerQQ != sender.user.id &&
                !ConfigOp.config.whitelist.contains((sender.user.id))
    }

    private fun isUnbind(sender: CommandSender): Boolean {
        return !UserData.userMap.contains(sender.user!!.id)
    }
}