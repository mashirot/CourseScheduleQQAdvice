package ski.mashiro.timer;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import org.apache.commons.io.FileUtils;
import ski.mashiro.CourseScheduleQQAdvice;
import ski.mashiro.config.Config;
import ski.mashiro.data.CourseData;
import ski.mashiro.pojo.Cache;
import ski.mashiro.pojo.Course;
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
    public synchronized void run() {
        try {
            for (String qq : Config.WHITELIST.getWhitelist()) {
                File dailyCourseCache = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "/Courses/" + qq, "dailyCourseCache" + ".json");
                if (!dailyCourseCache.exists()) {
                    continue;
                }
                Cache cache = ThreadController.OBJECT_MAPPER.readValue(FileUtils.readFileToString(dailyCourseCache, "utf-8"), Cache.class);
                List<Date[]> dateList = Utils.transToList(cache.getDateList(), Date[].class);
                if (dateList == null || dateList.size() == 0) {
                    continue;
                }
                Iterator<Date[]> it = dateList.listIterator();
                while (it.hasNext()) {
                    Date[] next = it.next();
                    if (next[0].getTime() < System.currentTimeMillis()) {
                        if (next[1].getTime() < System.currentTimeMillis()) {
                            it.remove();
                            cache.setIndex(cache.getIndex() + 1);
                            continue;
                        }
                        Bot bot = Bot.getInstance(Config.CONFIGURATION.getBot());
                        for (Friend friend : bot.getFriends()) {
                            if ((friend.getId() + "").equals(qq)) {
                                Course course = CourseData.DailyEffCourseList.get(cache.getIndex());
                                String sb = Utils.transferDateToStr(new Date()) + "   " + Utils.getWeek() + "\n" +
                                        "上课时间\t\t" + "上课地点\t\t" + "课程名\n" +
                                        course.getCourseDate().split(" ")[1] + "\t" + course.getCourseLocation() + "\t\t" + course.getCourseName();
                                friend.sendMessage(sb);
                            }
                        }
                        it.remove();
                        cache.setIndex(cache.getIndex() + 1);
                    }
                }
                cache.setDateList(dateList);
                FileUtils.write(dailyCourseCache, ThreadController.OBJECT_MAPPER.writeValueAsString(cache), "utf-8");
            }
        } catch (Exception e) {
            CourseScheduleQQAdvice.INSTANCE.getLogger().info("课前提醒线程发生异常");
            e.printStackTrace();
        }
    }
}
