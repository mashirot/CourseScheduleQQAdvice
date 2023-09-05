package ski.mashiro.command

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.command.isNotConsole
import net.mamoe.mirai.console.command.isUser
import ski.mashiro.CourseScheduleQQAdvice
import ski.mashiro.file.ConfigOp

object AdminCommand : CompositeCommand(
    CourseScheduleQQAdvice, "admin",
    description = "这是一个测试指令", // 设置描述，将会在 /help 展示
) {
    @ExperimentalCommandDescriptors
    override val prefixOptional: Boolean
        get() = true

    private val logger = CourseScheduleQQAdvice.logger

    @SubCommand("addWhitelist", "添加白名单")
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

    @SubCommand("delWhitelist", "移出白名单")
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

    @SubCommand("setOwner")
    suspend fun setApiAddress(sender: CommandSender, apiAddress: String) {
        if (hasNoPerm(sender)) {
            logger.warning("${sender.name} 尝试越权操作")
            return
        }
        ConfigOp.config.apiAddress = apiAddress
        sender.sendMessage("更改apiAddress成功")
    }

    private fun hasNoPerm(sender: CommandSender): Boolean {
        return sender.isNotConsole() && sender.isUser() && ConfigOp.config.ownerQQ != sender.user.id
    }
}