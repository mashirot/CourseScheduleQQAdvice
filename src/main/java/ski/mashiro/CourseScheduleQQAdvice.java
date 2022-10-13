package ski.mashiro;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

public final class CourseScheduleQQAdvice extends JavaPlugin {
    public static final CourseScheduleQQAdvice INSTANCE = new CourseScheduleQQAdvice();

    private CourseScheduleQQAdvice() {
        super(new JvmPluginDescriptionBuilder("ski.mashirocourse-schedule-qq-advice", "1.0.0")
                .name("CourseScheduleQQAdvice")
                .author("MashiroT")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");
    }
}