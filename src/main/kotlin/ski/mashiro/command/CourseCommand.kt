package ski.mashiro.command

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.console.command.isUser
import ski.mashiro.CourseScheduleQQAdvice
import ski.mashiro.constant.Consts
import ski.mashiro.data.CourseData
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

    @SubCommand("next", "n")
    suspend fun next(sender: CommandSender) {
        if (hasNoPerm(sender)) {
            return
        }
        if (isUnbind(sender)) {
            sender.sendMessage("请先绑定用户")
            return
        }
        val senderQQ = sender.user!!.id
        val courseQueue = CourseData.courseMap[senderQQ]!!
        if (courseQueue.isEmpty()) {
            sender.sendMessage("暂无课程")
            return
        }
        sender.sendMessage(MessageServiceImpl.getCourseMsg(courseQueue.peek()))
    }

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
        val result = CourseServiceImpl.getTodayCoursesFromServer(UserData.userMap[senderQQ]!!)
        if (result.code != Consts.COURSE_LIST_SUCCESS) {
            sender.sendMessage(result.msg!!)
            return
        }
        sender.sendMessage(MessageServiceImpl.getCoursesMsg(result.data!!))
    }

    @SubCommand("effective", "eff")
    suspend fun effCourse(sender: CommandSender) {
        if (hasNoPerm(sender)) {
            return
        }
        if (isUnbind(sender)) {
            sender.sendMessage("请先绑定用户")
            return
        }
        val senderQQ = sender.user!!.id
        val result = CourseServiceImpl.getEffCoursesFromServer(UserData.userMap[senderQQ]!!)
        if (result.code != Consts.COURSE_LIST_SUCCESS) {
            sender.sendMessage(result.msg!!)
            return
        }
        sender.sendMessage(MessageServiceImpl.getCoursesMsg(result.data!!))
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
        val result = CourseServiceImpl.getAllCoursesFromServer(UserData.userMap[senderQQ]!!)
        if (result.code != Consts.COURSE_LIST_SUCCESS) {
            sender.sendMessage(result.msg!!)
            return
        }
        sender.sendMessage(MessageServiceImpl.getCoursesMsg(result.data!!))
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