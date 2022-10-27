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

import java.util.List;

/**
 * @author MashiroT
 */
public class MorningThread implements Runnable {
    @Override
    public void run() {
        for (String qq : Config.WHITELIST.getWhitelist()) {
            Result todayEffSchedule = CourseData.getTodayEffSchedule((User) UserData.getUser(qq).getData(), qq);
            if (!todayEffSchedule.getCode().equals(Code.LIST_DATE_SUCCESS)) {
                continue;
            }
            try {
                List<Course> courseList = Utils.transToList(todayEffSchedule.getData(), Course.class);
                String schedule = Utils.printSchedule(courseList);
                Bot bot = Bot.getInstance(Config.CONFIGURATION.getBot());
                for (Friend friend : bot.getFriends()) {
                    if ((friend.getId() + "").equals(qq)) {
                        friend.sendMessage("早上好\n" +schedule);
                    }
                }
            } catch (Exception e) {
                CourseScheduleQQAdvice.INSTANCE.getLogger().info("早晨课程提醒线程发生了异常");
                e.printStackTrace();
            }
        }
    }
}
