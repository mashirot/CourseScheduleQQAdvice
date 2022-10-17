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

import java.util.*;
import java.util.concurrent.*;


/**
 * @author MashiroT
 */
public class TimerTask {
    private static int i = 0;
    private static int j = 0;
    public static void openTasks() {
        Calendar now = Calendar.getInstance();
        Calendar morning = Calendar.getInstance();
        if (now.get(Calendar.HOUR_OF_DAY) < 6) {
            morning.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 6, 0, 0);
        } else {
            morning.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH) + 1, 6, 0, 0);
        }
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

        pool.scheduleAtFixedRate(() -> {
            Thread.currentThread().setDaemon(true);
            CourseScheduleQQAdvice.INSTANCE.getLogger().info("现在的时间是：" + new Date() + " 早晨课程提醒线程启动了");
            if (i == 0) {
                i++;
                return;
            }
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
        }, Math.abs(morning.getTime().getTime() - now.getTime().getTime()), 1, TimeUnit.DAYS);

        pool.scheduleAtFixedRate(() -> {
            Thread.currentThread().setDaemon(true);
            if (j == 0) {
                j++;
                return;
            }
            try {
                for (String qq : Config.WHITELIST.getWhitelist()) {
                    List<Date> dateList = CourseData.beforeStartTimeList.get(qq);
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
                }
            } catch (Exception e) {
                CourseScheduleQQAdvice.INSTANCE.getLogger().info("课前提醒线程发生异常");
                e.printStackTrace();
            }

        }, 0, 1, TimeUnit.MINUTES);

    }

}
