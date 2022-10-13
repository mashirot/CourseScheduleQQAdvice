package ski.mashiro.command;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.FriendCommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import org.jetbrains.annotations.NotNull;
import ski.mashiro.CourseScheduleQQAdvice;

/**
 * @author MashiroT
 */
public class Command extends JCompositeCommand {

    public Command() {
        super(CourseScheduleQQAdvice.INSTANCE, "user");
        setPrefixOptional(true);
    }

    @SubCommand("bind")
    public void bind(@NotNull CommandSender sender, @NotNull String userCode, String passwd) {
        if (sender.getClass() == FriendCommandSender.class) {
            System.out.println(" Friend:  " + sender + "=>" + userCode);
        }
        CourseScheduleQQAdvice.INSTANCE.getLogger().info(sender + ": " + userCode + ", " + passwd);
    }

    @SubCommand("unbind")
    public void unbind(@NotNull CommandSender sender, @NotNull String userCode) {
        
    }
}
