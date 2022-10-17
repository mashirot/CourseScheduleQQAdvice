package ski.mashiro.timer;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import org.apache.commons.io.FileUtils;
import ski.mashiro.CourseScheduleQQAdvice;
import ski.mashiro.config.Config;
import ski.mashiro.data.CourseData;
import ski.mashiro.data.UserData;
import ski.mashiro.pojo.Code;
import ski.mashiro.pojo.Course;
import ski.mashiro.pojo.Result;
import ski.mashiro.pojo.User;
import ski.mashiro.util.Utils;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author MashiroT
 */
public class RemindThread implements Runnable{

    @Override
    public void run() {
        try {
            for (String qq : Config.WHITELIST.getWhitelist()) {
                File dailyCourseCache = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "/Courses/" + qq, "dailyCourseCache" + ".json");
                List<Date> dateList = Utils.transToList(ThreadController.OBJECT_MAPPER.readValue(FileUtils.readFileToString(dailyCourseCache, "utf-8"), List.class), Date.class);
                if (dateList == null || dateList.size() == 0) {
                    continue;
                }
                Iterator<Date> it = dateList.listIterator();
                while (it.hasNext()) {
                    if (it.next().getTime() < System.currentTimeMillis()) {
                        Bot bot = Bot.getInstance(Config.CONFIGURATION.getBot());
                        for (Friend friend : bot.getFriends()) {
                            if ((friend.getId() + "").equals(qq)) {
                                Result upcoming = CourseData.getUpcoming((User) UserData.getUser(friend.getId() + "").getData(), friend.getId() + "");
                                if (upcoming.getCode().equals(Code.NO_UPCOMING)) {
                                    continue;
                                }
                                Course course = (Course) upcoming.getData();
                                String sb = Utils.transitionDateToStr(new Date()) + "   " + Utils.getWeek() + "\n" +
                                        "上课时间\t\t" + "上课地点\t\t" + "课程名\n" +
                                        course.getCourseShowTime() + "\t" + course.getCourseLocation() + "\t\t" + course.getCourseName();
                                friend.sendMessage(sb);
                            }
                        }
                        it.remove();
                    }
                }
                FileUtils.write(dailyCourseCache, ThreadController.OBJECT_MAPPER.writeValueAsString(dateList), "utf-8");
            }
        } catch (Exception e) {
            CourseScheduleQQAdvice.INSTANCE.getLogger().info("课前提醒线程发生异常");
            e.printStackTrace();
        }
    }
}
