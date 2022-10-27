package ski.mashiro.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.FriendCommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import org.jetbrains.annotations.NotNull;
import ski.mashiro.CourseScheduleQQAdvice;
import ski.mashiro.config.Config;
import ski.mashiro.data.CourseData;
import ski.mashiro.data.UserData;
import ski.mashiro.pojo.Code;
import ski.mashiro.pojo.Course;
import ski.mashiro.pojo.Result;
import ski.mashiro.pojo.User;
import ski.mashiro.util.Utils;

import java.util.*;

/**
 * @author MashiroT
 */
public class CourseCommand extends JCompositeCommand {

    public CourseCommand() {
        super(CourseScheduleQQAdvice.INSTANCE, "course", "c");
        super.setPrefixOptional(true);
    }

    @SubCommand("all")
    @Description("所有课程")
    public void all(@NotNull CommandSender sender) {
        Result result = verifyIllegal(sender);
        if (!result.getCode().equals(Code.GET_USER_SUCCESS)) {
            return;
        }
        if (!hasPermission(sender)) {
            sender.sendMessage("无权限");
            return;
        }
        Result getCoursesRs = CourseData.getSchedule((User) result.getData(), Objects.requireNonNull(sender.getUser()).getId() + "");
        if (getCoursesRs.getCode().equals(Code.COURSE_FILE_CREATE_SUCCESS)) {
            try {
                List<Course> courseList = Utils.transToList(((Result) getCoursesRs.getData()).getData(), Course.class);
                String detailSchedule = printDetailSchedule(courseList);
                sender.sendMessage(detailSchedule);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else if (getCoursesRs.getCode().equals(Code.COURSE_FILE_CREATE_FAILED)) {
            sender.sendMessage("请求失败，请检查服务器地址");
        }
    }

    @SubCommand("eff")
    @Description("所有有效课程")
    public void allEff(@NotNull CommandSender sender) {
        Result result = verifyIllegal(sender);
        if (!result.getCode().equals(Code.GET_USER_SUCCESS)) {
            return;
        }
        if (!hasPermission(sender)) {
            sender.sendMessage("无权限");
            return;
        }
        Result getCoursesRs = CourseData.getEffSchedule((User) result.getData(), Objects.requireNonNull(sender.getUser()).getId() + "");
        if (getCoursesRs.getCode().equals(Code.COURSE_FILE_CREATE_SUCCESS)) {
            try {
                List<Course> courseList = Utils.transToList(((Result) getCoursesRs.getData()).getData(), Course.class);
                String detailSchedule = printDetailSchedule(courseList);
                sender.sendMessage(detailSchedule);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (getCoursesRs.getCode().equals(Code.COURSE_FILE_CREATE_FAILED)) {
            sender.sendMessage("请求失败，请检查服务器地址");
        }
    }

    @SubCommand({"today", "t"})
    @Description("获取今日课程")
    public void today(@NotNull CommandSender sender) {
        Result result = verifyIllegal(sender);
        if (!result.getCode().equals(Code.GET_USER_SUCCESS)) {
            return;
        }
        if (!hasPermission(sender)) {
            sender.sendMessage("无权限");
            return;
        }
        Result todayEffSchedule = CourseData.getTodayEffSchedule((User) result.getData(), Objects.requireNonNull(sender.getUser()).getId() + "");
        if (todayEffSchedule.getCode().equals(Code.LIST_DATE_SUCCESS)) {
            try {
                List<Course> courseList = Utils.transToList(todayEffSchedule.getData(), Course.class);
                String schedule = Utils.printSchedule(courseList);
                sender.sendMessage(schedule);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else if (todayEffSchedule.getCode().equals(Code.LIST_DATE_FAILED)) {
            sender.sendMessage("请求失败，请检查服务器地址");
        } else {
            sender.sendMessage("系统错误，错误代码：" + result.getCode());
        }

    }

    @SubCommand({"next", "upcoming", "n"})
    @Description("获取下节课的信息")
    public void upcoming(@NotNull CommandSender sender) {
        Result result = verifyIllegal(sender);
        if (!result.getCode().equals(Code.GET_USER_SUCCESS)) {
            return;
        }
        if (!hasPermission(sender)) {
            sender.sendMessage("无权限");
            return;
        }
        Result upcoming = CourseData.getUpcoming((User) result.getData(), Objects.requireNonNull(sender.getUser()).getId() + "");
        if (upcoming.getCode().equals(Code.NO_UPCOMING)) {
            sender.sendMessage("今日课程已全部结束");
            return;
        }
        if (upcoming.getCode().equals(Code.GET_UPCOMING_FAILED)) {
            sender.sendMessage("请先获取今日课程或检查数据文件占用");
            return;
        }
        if (upcoming.getCode().equals(Code.GET_UPCOMING_SUCCESS)) {
            Course course = (Course) upcoming.getData();
            String schedule = Utils.printSchedule(Collections.singletonList(course));
            sender.sendMessage(schedule);
        }
    }

    private String printDetailSchedule(List<Course> courseList) {
        StringBuilder sb = new StringBuilder("日期\t\t上课时间\t\t\t上课地点\t\t课程名\n");
        if (courseList.size() > 0) {
            String date = null;
            for (Course course : courseList) {
                if (date == null) {
                    date = course.getCourseDate().split(" ")[0];
                }
                if (!date.equals(course.getCourseDate().split(" ")[0])) {
                    date = course.getCourseDate().split(" ")[0];
                    sb.append("---------------").append("\n");
                }
                sb.append(course.getCourseDate().split(" ")[0]).append("\t").append(course.getCourseDate().split(" ")[1]).append("\t\t").append(course.getCourseLocation()).append("\t\t").append(course.getCourseName());
                if (!course.equals(courseList.get(courseList.size() - 1))) {
                    sb.append("\n");
                }
            }
        } else {
            sb.append("暂无课程信息");
        }
        return sb.toString();
    }

    private Result verifyIllegal(@NotNull CommandSender sender) {
        if (!(sender instanceof FriendCommandSender)) {
            sender.sendMessage("发送者只能为好友");
            return new Result(Code.SENDER_TYPE_ERR, null);
        }
        Result getUserRs = UserData.getUser(Objects.requireNonNull(sender.getUser()).getId() + "");
        if (getUserRs.getCode().equals(Code.GET_USER_FAILED)) {
            sender.sendMessage("获取用户信息失败，请检查是否绑定");
        }
        if (getUserRs.getCode().equals(Code.GET_USER_ERR)) {
            sender.sendMessage("请检查data目录下是否有多余文件");
        }
        return getUserRs;
    }

    public static boolean hasPermission(@NotNull CommandSender sender) {
        for (String s : Config.WHITELIST.getWhitelist()) {
            if (s.equals(Objects.requireNonNull(sender.getUser()).getId() + "")) {
                return true;
            }
        }
        return Config.CONFIGURATION.getOwner().equals(Objects.requireNonNull(sender.getUser()).getId());
    }

}
