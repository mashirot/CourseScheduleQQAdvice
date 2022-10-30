package ski.mashiro.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import ski.mashiro.CourseScheduleQQAdvice;
import ski.mashiro.config.Config;
import ski.mashiro.net.HttpRequest;
import ski.mashiro.pojo.*;
import ski.mashiro.util.Utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * @author MashiroT
 */
public class CourseData {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static List<Course> DailyEffCourseList = new ArrayList<>();
    public static Map<String,List<Date>> beforeStartTimeList = new HashMap<>();
    public static Map<String,List<Date>> courseEndTime = new HashMap<>();

    public static Result getSchedule(User user, String qq) {
        try {
            createUserCourseFolder(qq);
            File courseFile = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "/Courses/" + qq, user.getUserCode() + ".json");
            if (!courseFile.exists()) {
                if (!courseFile.createNewFile()) {
                    return new Result(Code.COURSE_FILE_CREATE_FAILED, null);
                }
            }
            Result result = HttpRequest.getSchedule(user);
            if (!result.getCode().equals(Code.LIST_ALL_SUCCESS)) {
                return new Result(Code.LIST_ALL_FAILED, null);
            }
            FileUtils.write(courseFile, OBJECT_MAPPER.writeValueAsString(result.getData()), "utf-8");
            return new Result(Code.COURSE_FILE_CREATE_SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(Code.COURSE_FILE_CREATE_FAILED, null);
    }

    public static Result getEffSchedule(User user, String qq) {
        try {
            createUserCourseFolder(qq);
            File courseFile = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "/Courses/" + qq, user.getUserCode() + "eff" + ".json");
            if (!courseFile.exists()) {
                if (!courseFile.createNewFile()) {
                    return new Result(Code.COURSE_FILE_CREATE_FAILED, null);
                }
            }
            Result result = HttpRequest.getEffSchedule(user);
            if (!result.getCode().equals(Code.LIST_ALL_SUCCESS)) {
                return new Result(Code.LIST_ALL_FAILED, null);
            }
            FileUtils.write(courseFile, OBJECT_MAPPER.writeValueAsString(result.getData()), "utf-8");
            return new Result(Code.COURSE_FILE_CREATE_SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(Code.COURSE_FILE_CREATE_FAILED, null);
    }

    public static Result getTodayEffSchedule(User user, String qq) {
        Result todayEffSchedule = HttpRequest.getTodayEffSchedule(user);
        if (todayEffSchedule.getCode().equals(Code.LIST_DATE_SUCCESS)) {
            try {
                File dailyEffFile = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "/Courses/" + qq, user.getUserCode() + "dailyEff" + ".json");
                FileUtils.write(dailyEffFile, OBJECT_MAPPER.writeValueAsString(todayEffSchedule.getData()), "utf-8");
                DailyEffCourseList = Utils.transToList(todayEffSchedule.getData(), Course.class);
                DailyEffCourseList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getCourseDate().split(" ")[1].split("-")[0].split(":")[0])));
                getBeforeCourseTime(qq);
                refreshDailyEffCache(qq);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return todayEffSchedule;
    }

    public static Result getTodaySchedule(User user) {
        return HttpRequest.getTodaySchedule(user);
    }

    public static Result getUpcoming(User user, String qq) {
        try {
            File dailyEffFile = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "/Courses/" + qq, user.getUserCode() + "dailyEff" + ".json");
            if (!dailyEffFile.exists()) {
                getTodayEffSchedule(user, qq);
            }
            String fileToString = FileUtils.readFileToString(dailyEffFile, "utf-8");
            DailyEffCourseList = Utils.transToList(OBJECT_MAPPER.readValue(fileToString, List.class), Course.class);
            DailyEffCourseList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getCourseDate().split(" ")[1].split("-")[0].split(":")[0])));
            if ("[]".equals(fileToString)) {
                return new Result(Code.NO_UPCOMING, null);
            }
            if (DailyEffCourseList.size() == 0) {
                return new Result(Code.GET_UPCOMING_FAILED, null);
            }
            getBeforeCourseTime(qq);
            List<Integer> rsList = new ArrayList<>();
            Calendar now = Calendar.getInstance();
            int cHour = now.get(Calendar.HOUR_OF_DAY);
            int cMinute = now.get(Calendar.MINUTE);
            Calendar beforeTime = Calendar.getInstance();
            for (Date beforeDate : beforeStartTimeList.get(qq)) {
                beforeTime.setTime(beforeDate);
                int sHour = beforeTime.get(Calendar.HOUR_OF_DAY);
                int sMinute = beforeTime.get(Calendar.MINUTE);
                if (sHour - cHour < 0) {
                    rsList.add(-1);
                    continue;
                }
                String minuteMinus = String.valueOf(Math.abs(sMinute - cMinute));
                rsList.add(Integer.parseInt((sHour - cHour) + (minuteMinus.length() == 2 ? "" : minuteMinus + "0")));
            }
            int temp = 0;
            for (Integer rs : rsList) {
                if (rs < 0) {
                    continue;
                }
                temp = temp == 0 ? rs : temp;
                temp = rs < temp ? rs : temp;
            }
            if (temp == 0) {
                return new Result(Code.NO_UPCOMING, null);
            }
            return new Result(Code.GET_UPCOMING_SUCCESS, DailyEffCourseList.get(rsList.indexOf(temp)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(Code.GET_UPCOMING_FAILED, null);
    }

    private static void createUserCourseFolder(String qq) throws IOException {
        File coursesFolder = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder(), "/Courses");
        if (!coursesFolder.exists()) {
            FileUtils.forceMkdir(coursesFolder);
        }
        File userCoursesFolder = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "/Courses", qq);
        if (!userCoursesFolder.exists()) {
            FileUtils.forceMkdir(userCoursesFolder);
        }
    }

    public static void initData() {
        try {
            for (String qq : Config.WHITELIST.getWhitelist()) {
                Result todayEffSchedule = getTodayEffSchedule((User) UserData.getUser(qq).getData(), qq);
                if (!todayEffSchedule.getCode().equals(Code.LIST_DATE_SUCCESS)) {
                    continue;
                }
                DailyEffCourseList = Utils.transToList(OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(todayEffSchedule.getData()), List.class), Course.class);
                DailyEffCourseList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getCourseDate().split(" ")[1].split("-")[0].split(":")[0])));
                if (DailyEffCourseList.size() == 0) {
                    return;
                }
                getBeforeCourseTime(qq);
                getEndCourseTime(qq);
                refreshDailyEffCache(qq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void refreshDailyEffCache(String qq) throws IOException {
        File dailyCourseCache = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "/Courses/" + qq, "dailyCourseCache" + ".json");
        if (courseEndTime.get(qq) == null) {
            if (!dailyCourseCache.exists()) {
                if (dailyCourseCache.createNewFile()) {
                    FileUtils.write(dailyCourseCache, OBJECT_MAPPER.writeValueAsString(new Cache(new ArrayList<>(0), 0)), "utf-8");
                    return;
                }
            }
            if (dailyCourseCache.delete()) {
                if (dailyCourseCache.createNewFile()) {
                    FileUtils.write(dailyCourseCache, OBJECT_MAPPER.writeValueAsString(new Cache(new ArrayList<>(0), 0)), "utf-8");
                }
            }
            return;
        }
        List<Date[]> dates = new ArrayList<>(courseEndTime.get(qq).size());
        int index = 0;
        for (Date date : courseEndTime.get(qq)) {
            dates.add(new Date[]{beforeStartTimeList.get(qq).get(index), date});
            index++;
        }
        if (!dailyCourseCache.exists()) {
            if (dailyCourseCache.createNewFile()) {
                FileUtils.write(dailyCourseCache, OBJECT_MAPPER.writeValueAsString(new Cache(dates, 0)), "utf-8");
                return;
            }
        }
        if (dailyCourseCache.delete()) {
            if (dailyCourseCache.createNewFile()) {
                FileUtils.write(dailyCourseCache, OBJECT_MAPPER.writeValueAsString(new Cache(dates, 0)), "utf-8");
            }
        }
    }

    private static void getBeforeCourseTime(String qq) throws ParseException {
        List<Date> dateList;
        if (beforeStartTimeList.get(qq) != null) {
            beforeStartTimeList.get(qq).clear();
            dateList = beforeStartTimeList.get(qq);
        } else {
            dateList = new ArrayList<>(DailyEffCourseList.size());
        }
        for (Course course : DailyEffCourseList) {
            Date beforeDate = Utils.transferStrToDate(course.getCourseDate().split(" ")[1].split("-")[0]);
            Calendar date = Calendar.getInstance();
            date.setTime(beforeDate);
            date.set(Calendar.MINUTE, -20);
            dateList.add(date.getTime());
        }
        beforeStartTimeList.put(qq, dateList);
    }

    private static void getEndCourseTime(String qq) throws ParseException {
        List<Date> dateList;
        if (courseEndTime.get(qq) != null) {
            courseEndTime.get(qq).clear();
            dateList = courseEndTime.get(qq);
        } else {
            dateList = new ArrayList<>(DailyEffCourseList.size());
        }
        for (Course course : DailyEffCourseList) {
            dateList.add(Utils.transferStrToDate(course.getCourseDate().split(" ")[1].split("-")[1]));
        }
        courseEndTime.put(qq, dateList);
    }

}
