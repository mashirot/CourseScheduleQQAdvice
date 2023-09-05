package ski.mashiro.command

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.console.command.isUser
import ski.mashiro.CourseScheduleQQAdvice
import ski.mashiro.data.UserData
import ski.mashiro.entity.User
import ski.mashiro.file.ConfigOp
import ski.mashiro.service.impl.UserServiceImpl

object UserCommand : CompositeCommand(
    CourseScheduleQQAdvice, "user", "u",
    description = "这是一个测试指令", // 设置描述，将会在 /help 展示
) {
    @net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
    override val prefixOptional: Boolean
        get() = true

    @SubCommand("bind", "b")
    suspend fun bindUser(sender: CommandSender, username: String, apiToken: String) {
        if (hasNoPerm(sender)) {
            return
        }
        if (username.length < 6 || apiToken.length < 32) {
            sender.sendMessage("非法参数")
            return
        }
        val senderQQ = sender.user!!.id
        if (UserData.userMap.contains(senderQQ)) {
            sender.sendMessage("不得重复绑定")
            return
        }
        if (!UserServiceImpl.verifyUser(username, apiToken)) {
            sender.sendMessage("用户名或token有误")
            return
        }
        UserData.userMap[senderQQ] = User(senderQQ, username, apiToken)
        sender.sendMessage("绑定用户: $username 成功")
    }

    @SubCommand("unbind", "ub")
    suspend fun unbindUser(sender: CommandSender) {
        if (hasNoPerm(sender)) {
            return
        }
        val senderQQ = sender.user!!.id
        if (!UserData.userMap.contains(senderQQ)) {
            sender.sendMessage("未绑定用户")
            return
        }
        val user = UserData.userMap.remove(senderQQ)
        sender.sendMessage("解绑用户: ${user!!.username} 成功")
    }

    private fun hasNoPerm(sender: CommandSender): Boolean {
        return sender.isConsole() || sender.isUser() &&
                ConfigOp.config.ownerQQ != sender.user.id &&
                !ConfigOp.config.whitelist.contains((sender.user.id))
    }
}