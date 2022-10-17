package ski.mashiro.timer;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import ski.mashiro.CourseScheduleQQAdvice;
import ski.mashiro.config.Config;
import ski.mashiro.data.CourseData;
import ski.mashiro.data.UserData;
import ski.mashiro.pojo.Code;
import ski.mashiro.pojo.Course;
import ski.mashiro.pojo.Result;
import ski.mashiro.pojo.User;
import ski.mashiro.util.Utils;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author MashiroT
 */
public class MorningThread implements Runnable {
    @Override
    public void run() {
        CourseScheduleQQAdvice.INSTANCE.getLogger().info("现在的时间是：" + new Date() + " 早晨课程提醒线程启动了");
        for (String qq : Config.WHITELIST.getWhitelist()) {
            Result todayEffSchedule = CourseData.getTodayEffSchedule((User) UserData.getUser(qq).getData(), qq);
            if (!todayEffSchedule.getCode().equals(Code.LIST_DATE_SUCCESS)) {
                continue;
            }
            try {
                List<Course> courseList = Utils.transToList(todayEffSchedule.getData(), Course.class);
                StringBuilder sb = new StringBuilder();
                sb.append("早上好\n");
                sb.append(Utils.transitionDateToStr(new Date())).append("   ").append(Utils.getWeek()).append("\n");
                if (courseList.size() > 0) {
                    sb.append("上课时间\t\t").append("上课地点\t\t").append("课程名\n");
                    courseList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getCourseShowTime().split("-")[0].split(":")[0])));
                    for (Course course : courseList) {
                        sb.append(course.getCourseShowTime()).append("\t").append(course.getCourseLocation()).append("\t\t").append(course.getCourseName());
                        if (!course.equals(courseList.get(courseList.size() - 1))) {
                            sb.append("\n");
                        }
                    }
                } else {
                    sb.append("今日无课,好好休息");
                }
                Bot bot = Bot.getInstance(Config.CONFIGURATION.getBot());
                for (Friend friend : bot.getFriends()) {
                    if ((friend.getId() + "").equals(qq)) {
                        friend.sendMessage(sb.toString());
                    }
                }
            } catch (Exception e) {
                CourseScheduleQQAdvice.INSTANCE.getLogger().info("早晨课程提醒线程发生了异常");
                e.printStackTrace();
            }
        }
    }
}
