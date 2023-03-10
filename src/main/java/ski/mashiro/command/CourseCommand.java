package ski.mashiro.command;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.FriendCommandSender;
import net.mamoe.mirai.console.command.SystemCommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import ski.mashiro.ScheduleQQAdvice;
import ski.mashiro.data.CourseData;
import ski.mashiro.dto.CourseDto;
import ski.mashiro.dto.CourseSearchDto;
import ski.mashiro.file.UserFile;
import ski.mashiro.file.WhitelistFile;
import ski.mashiro.pojo.User;
import ski.mashiro.util.FormatterUtil;

import java.time.LocalDate;
import java.util.List;

/**
 * @author MashiroT
 */
public class CourseCommand extends JCompositeCommand {

    public CourseCommand() {
        super(ScheduleQQAdvice.INSTANCE, "course", "c");
        super.setPrefixOptional(true);
    }

    @SubCommand("all")
    @Description("所有课程")
    public void all(CommandSender sender) {
        if (hasNoPermission(sender) || hasNotBind(sender)) {
            return;
        }
        User currUser = UserFile.USERS_MAP.get(sender.getUser().getId());
        CourseSearchDto searchDto = new CourseSearchDto(currUser.getUid());
        List<CourseDto> courseDtoList = CourseData.getCourse(searchDto);
        String courseInfo = FormatterUtil.detailFormat(courseDtoList);
        sender.sendMessage(courseInfo);
    }

    @SubCommand("eff")
    @Description("所有有效课程")
    public void allEff(CommandSender sender) {
        if (hasNoPermission(sender) || hasNotBind(sender)) {
            return;
        }
        User currUser = UserFile.USERS_MAP.get(sender.getUser().getId());
        CourseSearchDto searchDto = new CourseSearchDto(currUser.getUid(), currUser.getTermStartDate());
        List<CourseDto> courseDtoList = CourseData.getCourse(searchDto);
        String courseInfo = FormatterUtil.detailFormat(courseDtoList);
        sender.sendMessage(courseInfo);
    }

    @SubCommand({"today", "t"})
    @Description("获取今日课程")
    public void today(CommandSender sender) {
        if (hasNoPermission(sender) || hasNotBind(sender)) {
            return;
        }
        User currUser = UserFile.USERS_MAP.get(sender.getUser().getId());
        CourseSearchDto searchDto = new CourseSearchDto(currUser.getUid(), FormatterUtil.INT_DAY_TO_STR_MAP.get(LocalDate.now().getDayOfWeek().getValue()), currUser.getTermStartDate());
        List<CourseDto> courseDtoList = CourseData.getCourse(searchDto);
        String courseInfo = FormatterUtil.normalFormat(courseDtoList, null);
        sender.sendMessage(courseInfo);
    }

    @SubCommand({"refresh", "rt"})
    @Description("刷新课程队列")
    public void refresh(CommandSender sender) {
        if (hasNoPermission(sender) || hasNotBind(sender)) {
            return;
        }
        if (CourseData.refreshQueue(sender.getUser().getId())) {
            sender.sendMessage("刷新成功");
            return;
        }
        sender.sendMessage("Server Err");
    }

    @SubCommand({"next", "n"})
    @Description("获取下节课的信息")
    public void upcoming(CommandSender sender) {
        if (hasNoPermission(sender) || hasNotBind(sender)) {
            return;
        }
        if (CourseData.USER_TODAY_COURSE.get(sender.getUser().getId()).isEmpty()) {
            sender.sendMessage("今日课程已全部结束");
        }
        String courseInfo = FormatterUtil.normalFormat(List.of(CourseData.USER_TODAY_COURSE.get(sender.getUser().getId()).peek()), null);
        sender.sendMessage(courseInfo);
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