package ski.mashiro.command;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.FriendCommandSender;
import net.mamoe.mirai.console.command.SystemCommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import org.jetbrains.annotations.NotNull;
import ski.mashiro.CourseScheduleQQAdvice;
import ski.mashiro.config.Config;

import java.util.*;

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
        if (!(sender instanceof FriendCommandSender)) {
            sender.sendMessage("发送者只能为好友");
            return;
        }
        if (!Config.CONFIGURATION.getOwner().equals(Objects.requireNonNull(sender.getUser()).getId())) {
            sender.sendMessage("无权限");
            return;
        }
        String[] whitelistArray = Config.WHITELIST.getWhitelist();
        List<String> whitelist = new ArrayList<>(Arrays.asList(whitelistArray));
        for (String s : whitelist) {
            if (s.equals(qq)) {
                return;
            }
        }
        whitelist.add(qq);
        String[] rs = new String[whitelist.size()];
        for (int i = 0; i < whitelist.size(); i++) {
            rs[i] = whitelist.get(i);
        }
        Config.WHITELIST.setWhitelist(rs);
        Config.saveConfig();
        sender.sendMessage("添加成功");
    }

    @SubCommand("del")
    @Description("删除白名单")
    public void delWhitelist(@NotNull CommandSender sender, String qq) {
        if (!(sender instanceof FriendCommandSender)) {
            sender.sendMessage("发送者只能为好友");
            return;
        }
        if (!Config.CONFIGURATION.getOwner().equals(Objects.requireNonNull(sender.getUser()).getId())) {
            sender.sendMessage("无权限");
            return;
        }
        String[] whitelistArray = Config.WHITELIST.getWhitelist();
        List<String> whitelist = new ArrayList<>(Arrays.asList(whitelistArray));
        Iterator<String> it = whitelist.listIterator();
        while (it.hasNext()) {
            String item = it.next();
            if (item.equals(qq)) {
                it.remove();
                sender.sendMessage("删除成功");
                String[] rs = new String[whitelist.size()];
                for (int i = 0; i < whitelist.size(); i++) {
                    rs[i] = whitelist.get(i);
                }
                Config.WHITELIST.setWhitelist(rs);
                Config.saveConfig();
                return;
            }
        }
        sender.sendMessage("不在名单内");
    }

    @SubCommand("reload")
    public void reload(@NotNull CommandSender sender) {
        if (!(sender instanceof SystemCommandSender)) {
            if (!(sender instanceof FriendCommandSender)) {
                sender.sendMessage("发送者只能为好友");
                return;
            }
            if (!Config.CONFIGURATION.getOwner().equals(Objects.requireNonNull(sender.getUser()).getId())) {
                sender.sendMessage("无权限");
                return;
            }
        }
        Config.loadConfig();
        sender.sendMessage("重载成功");
    }
}
