package ski.mashiro.command;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.FriendCommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import org.jetbrains.annotations.NotNull;
import ski.mashiro.CourseScheduleQQAdvice;
import ski.mashiro.data.UserData;
import ski.mashiro.pojo.Code;
import ski.mashiro.pojo.Result;
import ski.mashiro.pojo.User;

import java.util.Objects;

/**
 * @author MashiroT
 */
public class UserCommand extends JCompositeCommand {

    public UserCommand() {
        super(CourseScheduleQQAdvice.INSTANCE, "user", "u");
        super.setPrefixOptional(true);
    }

    @SubCommand("bind")
    @Description("绑定用户")
    public void bind(@NotNull CommandSender sender, @NotNull String userCode, String apiToken) {
        if (!(sender instanceof FriendCommandSender)) {
            sender.sendMessage("发送者只能为好友");
            return;
        }
        if (!CourseCommand.hasPermission(sender)) {
            sender.sendMessage("无权限");
            return;
        }
        if (!userCode.matches("\\d{3,30}") || apiToken.length() != 32) {
            sender.sendMessage("学号或密钥格式有误");
            return;
        }
        Result result = UserData.saveUser(userCode, apiToken, Objects.requireNonNull(sender.getUser()).getId() + "");
        if (result.getCode().equals(Code.USER_FILE_CREATE_SUCCESS)) {
            sender.sendMessage("绑定成功");
        } else if (result.getCode().equals(Code.GET_USER_FAILED)) {
            sender.sendMessage("学号或密钥错误，登陆失败");
        } else if (result.getCode().equals(Code.USER_FILE_CREATE_FAILED) || result.getCode().equals(Code.COURSE_FILE_CREATE_FAILED)) {
            sender.sendMessage("文件创建失败，请检查文件目录，错误代码：" + result.getCode());
        } else {
            sender.sendMessage("系统错误，错误代码：" + result.getCode());
        }
    }
    @SubCommand("unbind")
    @Description("解除绑定")
    public void unbind(@NotNull CommandSender sender, @NotNull String userCode) {
        if (!(sender instanceof FriendCommandSender)) {
            sender.sendMessage("发送者只能为好友");
            return;
        }
        if (!CourseCommand.hasPermission(sender)) {
            sender.sendMessage("无权限");
            return;
        }
        if (!userCode.matches("\\d{3,30}")) {
            sender.sendMessage("学号格式有误");
            return;
        }
        Result result = UserData.delUser(userCode, Objects.requireNonNull(sender.getUser()).getId() + "");
        if (result.getCode().equals(Code.DELETE_USER_SUCCESS)) {
            sender.sendMessage("解绑成功");
        } else if (result.getCode().equals(Code.DELETE_USER_FAILED)) {
            sender.sendMessage("解绑失败，请检查文件占用");
        } else {
            sender.sendMessage("系统错误，错误代码：" + result.getCode());
        }
    }

    @SubCommand("list")
    @Description("列出绑定用户")
    public void list(@NotNull CommandSender sender) {
        if (!(sender instanceof FriendCommandSender)) {
            sender.sendMessage("发送者只能为好友");
            return;
        }
        if (!CourseCommand.hasPermission(sender)) {
            sender.sendMessage("无权限");
            return;
        }
        Result result = UserData.getUser(Objects.requireNonNull(sender.getUser()).getId() + "");
        if (result.getCode().equals(Code.GET_USER_FAILED)) {
            sender.sendMessage("获取用户信息失败，请检查是否绑定");
        }
        if (result.getCode().equals(Code.GET_USER_ERR)) {
            sender.sendMessage("请检查data目录下是否有多余文件");
        }
        User user = (User) result.getData();
        sender.sendMessage("学号：" + user.getUserCode());
    }
}
