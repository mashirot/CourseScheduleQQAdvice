package ski.mashiro;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.permission.AbstractPermitteeId;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import ski.mashiro.command.AdminCommand;
import ski.mashiro.command.CourseCommand;
import ski.mashiro.command.UserCommand;
import ski.mashiro.file.ConfigFile;
import ski.mashiro.file.UserFile;
import ski.mashiro.file.WhitelistFile;
import ski.mashiro.timer.Timer;

/**
 * @author MashiroT
 */
public final class ScheduleQQAdvice extends JavaPlugin {
    public static final ScheduleQQAdvice INSTANCE = new ScheduleQQAdvice();

    private ScheduleQQAdvice() {
        super(new JvmPluginDescriptionBuilder("ski.mashiro.schedule-advice", "2.0.0")
                .name("ScheduleQQAdvice")
                .author("MashiroT")
                .build());
    }

    @Override
    public void onEnable() {
        ConfigFile.loadConfig();
        WhitelistFile.loadWhitelist();
        UserFile.loadUsers();
        Timer.startTimer();
        CommandManager.INSTANCE.registerCommand(new AdminCommand(), true);
        CommandManager.INSTANCE.registerCommand(new UserCommand(), true);
        CommandManager.INSTANCE.registerCommand(new CourseCommand(), true);
        PermissionService.permit0(AbstractPermitteeId.AnyContact.INSTANCE, INSTANCE.getParentPermission());
        getLogger().info("CourseScheduleQQAdvice 加载成功!");
    }

    @Override
    public void onDisable() {
        ConfigFile.saveConfig();
        WhitelistFile.saveWhitelist();
        UserFile.saveUsers();
        getLogger().info("CourseScheduleQQAdvice 已卸载!");
    }
}