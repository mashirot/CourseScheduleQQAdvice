package ski.mashiro.command

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.command.isNotConsole
import net.mamoe.mirai.console.command.isUser
import ski.mashiro.CourseScheduleQQAdvice
import ski.mashiro.data.CourseData
import ski.mashiro.data.UserData
import ski.mashiro.file.ConfigOp
import java.time.LocalDateTime

object AdminCommand : CompositeCommand(
    CourseScheduleQQAdvice, "admin"
) {
    @ExperimentalCommandDescriptors
    override val prefixOptional: Boolean
        get() = true

    private val logger = CourseScheduleQQAdvice.logger

    @SubCommand("addWhitelist")
    suspend fun addWhitelist(sender: CommandSender, qq: Long) {
        if (hasNoPerm(sender)) {
            logger.warning("${sender.name} 尝试越权操作")
            return
        }
        if (ConfigOp.config.whitelist.contains(qq)) {
            sender.sendMessage("该用户已在白名单内")
            return
        }
        ConfigOp.config.whitelist.add(qq)
        sender.sendMessage("添加成功")
    }

    @SubCommand("delWhitelist")
    suspend fun delWhitelist(sender: CommandSender, qq: Long) {
        if (hasNoPerm(sender)) {
            logger.warning("${sender.name} 尝试越权操作")
            return
        }
        if (ConfigOp.config.whitelist.contains(qq)) {
            sender.sendMessage("该用户不在白名单内")
            return
        }
        ConfigOp.config.whitelist.remove(qq)
        sender.sendMessage("移除成功")
    }

    @SubCommand("setOwner")
    suspend fun setOwner(sender: CommandSender, qq: Long) {
        if (hasNoPerm(sender)) {
            logger.warning("${sender.name} 尝试越权操作")
            return
        }
        ConfigOp.config.ownerQQ = qq
        sender.sendMessage("更改Owner成功")
    }

    @SubCommand("setApiAddress")
    suspend fun setApiAddress(sender: CommandSender, apiAddress: String) {
        if (hasNoPerm(sender)) {
            logger.warning("${sender.name} 尝试越权操作")
            return
        }
        ConfigOp.config.apiAddress = apiAddress
        sender.sendMessage("更改apiAddress成功")
    }

    @SubCommand("refresh", "ref")
    suspend fun refreshCourseData(sender: CommandSender) {
        if (hasNoPerm(sender)) {
            logger.warning("${sender.name} 尝试越权操作")
            return
        }
        val now = LocalDateTime.now()
        UserData.userMap.values.forEach { user ->
            CourseData.getCourseData(now, user)
        }
        sender.sendMessage("刷新成功")
    }

    private fun hasNoPerm(sender: CommandSender): Boolean {
        return sender.isNotConsole() && sender.isUser() && ConfigOp.config.ownerQQ != sender.user.id
    }
}