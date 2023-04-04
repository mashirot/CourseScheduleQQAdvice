package ski.mashiro.command;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.FriendCommandSender;
import net.mamoe.mirai.console.command.SystemCommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import ski.mashiro.ScheduleQQAdvice;
import ski.mashiro.data.CourseData;
import ski.mashiro.data.UserData;
import ski.mashiro.file.UserFile;
import ski.mashiro.file.WhitelistFile;
import ski.mashiro.pojo.User;

import java.util.ArrayDeque;

/**
 * @author MashiroT
 */
public class UserCommand extends JCompositeCommand {
    public UserCommand() {
        super(ScheduleQQAdvice.INSTANCE, "user", "u");
        super.setPrefixOptional(true);
    }

    @SubCommand("bind")
    @Description("绑定用户")
    public void bind(CommandSender sender, String username, String apiToken) {
        if (hasNoPermission(sender)) {
            return;
        }
        if (UserFile.USERS_MAP.containsKey(sender.getUser().getId())) {
            sender.sendMessage("已经绑定了一个账户");
            return;
        }
        User rs = UserData.getUser(new User(sender.getUser().getId(), username, apiToken));
        if (rs == null) {
            sender.sendMessage("绑定失败");
            return;
        }
        rs.setApiToken(apiToken);
        UserFile.USERS.add(rs);
        UserFile.USERS_MAP.put(rs.getQq(), rs);
        CourseData.USER_TODAY_COURSE.putIfAbsent(rs.getQq(), new ArrayDeque<>());
        sender.sendMessage("绑定成功");
        if (CourseData.refreshQueue(sender.getUser().getId())) {
            sender.sendMessage("课程加载成功");
            return;
        }
        sender.sendMessage("Server Err");
    }

    @SubCommand("unbind")
    @Description("解除绑定")
    public void unbind(CommandSender sender) {
        if (hasNoPermission(sender) || hasNotBind(sender)) {
            return;
        }
        if (!UserFile.USERS_MAP.containsKey(sender.getUser().getId())) {
            sender.sendMessage("未绑定");
            return;
        }
        UserFile.USERS.removeIf(o -> o.getQq().equals(sender.getUser().getId()));
        UserFile.USERS_MAP.remove(sender.getUser().getId());
        sender.sendMessage("解绑成功");
        CourseData.USER_TODAY_COURSE.remove(sender.getUser().getId());
    }

    private boolean hasNoPermission(CommandSender sender) {
        if (!(sender instanceof SystemCommandSender)) {
            if (!(sender instanceof FriendCommandSender)) {
                return true;
            }
        }
        return !WhitelistFile.WHITELIST.contains(sender.getUser().getId());
    }

    private boolean hasNotBind(CommandSender sender) {
        return !UserFile.USERS_MAP.containsKey(sender.getUser().getId());
    }
}
