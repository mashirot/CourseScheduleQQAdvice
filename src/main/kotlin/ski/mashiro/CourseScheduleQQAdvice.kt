package ski.mashiro

import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import ski.mashiro.command.AdminCommand
import ski.mashiro.command.CourseCommand
import ski.mashiro.command.UserCommand
import ski.mashiro.file.ConfigOp

object CourseScheduleQQAdvice : KotlinPlugin(
    JvmPluginDescription(
        id = "ski.mashiro.course-schedule-advice",
        name = "CourseScheduleQQAdvice",
        version = "3.0.0",
    ) {
        author("MashiroT")
    }
) {
    override fun onEnable() {
        ConfigOp.loadConfig()
        CommandManager.registerCommand(AdminCommand)
        CommandManager.registerCommand(UserCommand)
        CommandManager.registerCommand(CourseCommand)
        AbstractPermitteeId.AnyContact.permit(parentPermission)
        logger.info { "CourseScheduleQQAdvice enabled" }
    }

    override fun onDisable() {
        ConfigOp.saveConfig()
        logger.info { "CourseScheduleQQAdvice disabled" }
    }
}