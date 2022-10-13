package ski.mashiro.command;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import org.jetbrains.annotations.NotNull;
import ski.mashiro.CourseScheduleQQAdvice;
import ski.mashiro.config.Config;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author MashiroT
 */
public class AdminCommand extends JCompositeCommand {

    public AdminCommand() {
        super(CourseScheduleQQAdvice.INSTANCE, "admin");
    }

    @SubCommand("add")
    @Description("增加白名单")
    public void addWhitelist(@NotNull CommandSender sender, String qq) {
        if (!Config.CONFIGURATION.getOwner().equals(Objects.requireNonNull(sender.getUser()).getId())) {
            sender.sendMessage("无权限");
            return;
        }
        List<String> whitelist = Config.WHITELIST.getWhitelist();
        for (String s : whitelist) {
            if (s.equals(qq)) {
                sender.sendMessage("重复添加");
                return;
            }
        }
        whitelist.add(qq);
        Config.saveConfig();
    }

    @SubCommand("del")
    @Description("删除白名单")
    public void delWhitelist(@NotNull CommandSender sender, String qq) {
        if (!Config.CONFIGURATION.getOwner().equals(Objects.requireNonNull(sender.getUser()).getId())) {
            sender.sendMessage("无权限");
            return;
        }
        List<String> whitelist = Config.WHITELIST.getWhitelist();
        Iterator<String> it = whitelist.listIterator();
        while (it.hasNext()) {
            String item = it.next();
            if (item.equals(qq)) {
                it.remove();
                sender.sendMessage("删除成功");
                return;
            }
        }
        sender.sendMessage("不在名单内");
        Config.saveConfig();
    }

    @SubCommand("test")
    public void test(@NotNull CommandSender sender) {
        sender.sendMessage("test");
    }
}
