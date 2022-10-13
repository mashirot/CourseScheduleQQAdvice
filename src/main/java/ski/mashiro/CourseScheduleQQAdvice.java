package ski.mashiro;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.permission.AbstractPermitteeId;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.BotEvent;
import ski.mashiro.command.AdminCommand;
import ski.mashiro.command.CourseCommand;
import ski.mashiro.command.UserCommand;
import ski.mashiro.config.Config;
import ski.mashiro.data.CourseData;
import ski.mashiro.listener.MessageListener;
import ski.mashiro.net.Update;
import ski.mashiro.timer.TimerTask;

/**
 * @author MashiroT
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public final class CourseScheduleQQAdvice extends JavaPlugin {
    public static final CourseScheduleQQAdvice INSTANCE = new CourseScheduleQQAdvice();

    private CourseScheduleQQAdvice() {
        super(new JvmPluginDescriptionBuilder("ski.mashiro.course-schedule-qq-advice", "1.0.0")
                .name("CourseScheduleQQAdvice")
                .author("MashiroT")
                .build());
    }

    @Override
    public void onEnable() {
        Config.loadConfig();
        Update.checkUpdate();
        EventChannel<Event> eventChannel = GlobalEventChannel.INSTANCE.filter(event -> event instanceof BotEvent && ((BotEvent) event).getBot().getId() == Config.CONFIGURATION.getBot());
        eventChannel.registerListenerHost(new MessageListener());
        CommandManager.INSTANCE.registerCommand(new UserCommand(), true);
        CommandManager.INSTANCE.registerCommand(new CourseCommand(), true);
        CommandManager.INSTANCE.registerCommand(new AdminCommand(), true);
        PermissionService.permit0(AbstractPermitteeId.AnyContact.INSTANCE, INSTANCE.getParentPermission());
        CourseData.initData();
        TimerTask.openTasks();
        getLogger().info("CourseScheduleQQAdvice 加载成功!");
    }

    @Override
    public void onDisable() {
        Config.saveConfig();
        getLogger().info("CourseScheduleQQAdvice 已卸载!");
    }
}