package ski.mashiro.command;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.FriendCommandSender;
import net.mamoe.mirai.console.command.SystemCommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import org.jetbrains.annotations.NotNull;
import ski.mashiro.ScheduleQQAdvice;
import ski.mashiro.file.ConfigFile;
import ski.mashiro.file.WhitelistFile;

/**
 * @author MashiroT
 */
public class AdminCommand extends JCompositeCommand {
    public AdminCommand() {
        super(ScheduleQQAdvice.INSTANCE, "admin");
        super.setPrefixOptional(true);
    }

    @SubCommand("add")
    @Description("增加白名单")
    public void addWhitelist(CommandSender sender, String qq) {
        if (hasNoPermission(sender)) {
            return;
        }
        WhitelistFile.WHITELIST.add(Long.valueOf(qq));
        sender.sendMessage("添加成功");
    }

    @SubCommand("del")
    @Description("删除白名单")
    public void delWhitelist(CommandSender sender, String qq) {
        if (hasNoPermission(sender)) {
            return;
        }
        for (int i = 0; i < WhitelistFile.WHITELIST.size(); i++) {
            if (WhitelistFile.WHITELIST.get(i).equals(Long.valueOf(qq))) {
                WhitelistFile.WHITELIST.remove(i);
                sender.sendMessage("删除成功");
                return;
            }
        }
        sender.sendMessage("不在名单内");
    }

    @SubCommand("setLeadTime")
    @Description("设置提前提醒时间")
    public void setLeadTime(CommandSender sender, int leadTime) {
        if (hasNoPermission(sender)) {
            return;
        }
        if (leadTime < 0) {
            sender.sendMessage("leadTime >= 0");
            return;
        }
        ConfigFile.CONFIG.setLeadTime(leadTime);
        sender.sendMessage("修改成功, 插件重新加载后生效");
    }

    @SubCommand("setUrl")
    @Description("设置接口地址")
    public void setUrl(CommandSender sender, String url) {
        if (hasNoPermission(sender)) {
            return;
        }
        ConfigFile.CONFIG.setUrl(url);
        sender.sendMessage("修改成功, 请确保地址格式正确, 插件重新加载后生效");
    }

    @SubCommand("reload")
    public void reload(@NotNull CommandSender sender) {
        if (hasNoPermission(sender)) {
            return;
        }
        ConfigFile.loadConfig();
        sender.sendMessage("重载成功");
    }

    private boolean hasNoPermission(CommandSender sender) {
        if (!(sender instanceof SystemCommandSender)) {
            if (!(sender instanceof FriendCommandSender)) {
                return true;
            }
        }
        return ConfigFile.CONFIG.getOwner() != sender.getUser().getId();
    }
}
