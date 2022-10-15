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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author MashiroT
 */
public class CourseData {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static List<Course> DailyEffCourseList = new ArrayList<>();
    public static Map<String,List<Date>> beforeStartTimeList = new HashMap<>();

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
            return new Result(Code.COURSE_FILE_CREATE_SUCCESS, null);
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
            return new Result(Code.COURSE_FILE_CREATE_SUCCESS, null);
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
                DailyEffCourseList.sort((o1, o2) -> Integer.parseInt(o1.getCourseShowTime().split("-")[0].split(":")[0]) - Integer.parseInt(o2.getCourseShowTime().split("-")[0].split(":")[0]));

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
                return new Result(Code.GET_UPCOMING_FAILED, null);
            }
            String fileToString = FileUtils.readFileToString(dailyEffFile, "utf-8");
            DailyEffCourseList = Utils.transToList(OBJECT_MAPPER.readValue(fileToString, List.class), Course.class);
            DailyEffCourseList.sort((o1, o2) -> Integer.parseInt(o1.getCourseShowTime().split("-")[0].split(":")[0]) - Integer.parseInt(o2.getCourseShowTime().split("-")[0].split(":")[0]));
            if ("[]".equals(fileToString)) {
                return new Result(Code.NO_UPCOMING, null);
            }
            if (DailyEffCourseList.size() == 0) {
                return new Result(Code.GET_UPCOMING_FAILED, null);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            Calendar currentTime = Calendar.getInstance();
            List<String> rsList = new ArrayList<>();
            currentTime.setTime(new Date());
            int cHour = currentTime.get(Calendar.HOUR_OF_DAY);
            int cMinute = currentTime.get(Calendar.MINUTE);
            Calendar startTime = Calendar.getInstance();
            List<Date> dateList;
            if (beforeStartTimeList.get(qq) != null) {
                beforeStartTimeList.get(qq).clear();
                dateList = beforeStartTimeList.get(qq);
            } else {
                dateList = new ArrayList<>();
            }
            for (Course course : DailyEffCourseList) {
                Date startTimeDate = sdf.parse(course.getCourseShowTime().split("-")[0]);
                startTime.setTime(startTimeDate);
                int sHour = startTime.get(Calendar.HOUR_OF_DAY);
                int sMinute = startTime.get(Calendar.MINUTE);
                Calendar date = Calendar.getInstance();
                date.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH), startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE) - 15, 0);
                dateList.add(date.getTime());
                if (sHour - cHour < 0) {
                    rsList.add("-1");
                    continue;
                }
                rsList.add((sHour - cHour) + Math.abs(sMinute - cMinute) + "");
            }
            beforeStartTimeList.put(qq, dateList);
            int temp = -2;
            for (String s : rsList) {
                temp = Math.max(Integer.parseInt(s), temp);
            }
            if (temp < 0) {
                return new Result(Code.NO_UPCOMING, null);
            }
            return new Result(Code.GET_UPCOMING_SUCCESS, DailyEffCourseList.get(rsList.indexOf(temp + "")));
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
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                DailyEffCourseList = Utils.transToList(OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(todayEffSchedule.getData()), List.class), Course.class);
                DailyEffCourseList.sort((o1, o2) -> Integer.parseInt(o1.getCourseShowTime().split("-")[0].split(":")[0]) - Integer.parseInt(o2.getCourseShowTime().split("-")[0].split(":")[0]));
                if (DailyEffCourseList.size() == 0) {
                    return;
                }
                Calendar now = Calendar.getInstance();
                Calendar beforeTime = Calendar.getInstance();
                List<Date> dateList;
                if (beforeStartTimeList.get(qq) != null) {
                    beforeStartTimeList.get(qq).clear();
                    dateList = beforeStartTimeList.get(qq);
                } else {
                    dateList = new ArrayList<>();
                }
                for (Course course : DailyEffCourseList) {
                    String startTime = course.getCourseShowTime().split("-")[0];
                    beforeTime.setTime(sdf.parse(startTime));
                    beforeTime.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), Calendar.MINUTE, beforeTime.get(Calendar.MINUTE) - 15, 0);
                    dateList.add(beforeTime.getTime());
                }
                beforeStartTimeList.put(qq, dateList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
