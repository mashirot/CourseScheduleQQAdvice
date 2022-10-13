package ski.mashiro.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import ski.mashiro.CourseScheduleQQAdvice;
import ski.mashiro.net.HttpRequest;
import ski.mashiro.pojo.Code;
import ski.mashiro.pojo.Result;
import ski.mashiro.pojo.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author MashiroT
 */
public class UserData {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static Result saveUser(String userCode, String apiToken, String qq) {
        try {
            File usersFolder = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder(), "Users");
            if (!usersFolder.exists()) {
                FileUtils.forceMkdir(usersFolder);
            }
            File userFolder = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "\\Users", qq);
            if (!userFolder.exists()) {
                FileUtils.forceMkdir(userFolder);
            }
            User user = new User(userCode, apiToken);
            Result result = HttpRequest.getEffSchedule(user);
            if (!result.getCode().equals(Code.LIST_ALL_SUCCESS)) {
                return new Result(Code.GET_USER_FAILED, null);
            }
            File userFile = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "\\Users\\" + qq, userCode + ".json");
            if (userFile.exists() || !userFile.createNewFile()) {
                delCache(user.getUserCode(), qq);
                return new Result(Code.USER_FILE_CREATE_FAILED, null);
            }
            FileUtils.write(userFile, OBJECT_MAPPER.writeValueAsString(user), "utf-8");

            Result rs = CourseData.getSchedule(user, qq);
            if (rs.getCode().equals(Code.COURSE_FILE_CREATE_FAILED)) {
                delCache(userCode, qq);
                return rs;
            }
            rs = CourseData.getEffSchedule(user, qq);
            if (rs.getCode().equals(Code.COURSE_FILE_CREATE_FAILED)) {
                delCache(userCode, qq);
                return rs;
            }
            return new Result(Code.USER_FILE_CREATE_SUCCESS, null);
        } catch (Exception e) {
            delCache(userCode, qq);
            e.printStackTrace();
        }
        return new Result(Code.USER_FILE_CREATE_FAILED, null);
    }

    public static Result delUser(String userCode, String qq) {
        File[] userFolder = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "\\Users\\" + qq).listFiles();
        File[] userCoursesFolder = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "\\Courses\\" + qq).listFiles();
        if (userFolder == null || userCoursesFolder == null) {
            return new Result(Code.FOLDER_OPEN_ERR, null);
        }
        boolean flag = false;
        try {
            flag = delFileRs(userCode, userFolder, flag);
            flag = delFileRs(userCode, userCoursesFolder, flag);
        } catch (Exception e) {
            return new Result(Code.DELETE_USER_FAILED, null);
        }
        return new Result(flag ? Code.DELETE_USER_SUCCESS : Code.DELETE_USER_FAILED, null);
    }

    private static boolean delFileRs(String userCode, File[] folder, boolean flag) {
        List<File> coursesFolderList = new ArrayList<>(Arrays.asList(folder));
        Iterator<File> courseIterator = coursesFolderList.iterator();
        while (courseIterator.hasNext()) {
            File file = courseIterator.next();
            if (file.isDirectory()) {
                continue;
            }
            if (file.getName().contains(userCode)) {
                flag = file.delete();
                courseIterator.remove();
            }
        }
        return flag;
    }

    public static Result getUser(String qq) {
        File[] userFolder = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "\\Users\\" + qq).listFiles();
        if (userFolder == null) {
            return new Result(Code.GET_USER_FAILED, null);
        }
        if (userFolder.length != 1) {
            return new Result(Code.GET_USER_ERR, null);
        }
        StringBuilder sb = new StringBuilder();
        try {
            for (File file : userFolder) {
                BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));
                String len;
                while ((len = br.readLine()) != null) {
                    sb.append(len);
                }
            }
            User user = OBJECT_MAPPER.readValue(sb.toString(), User.class);
            if (user != null) {
                return new Result(Code.GET_USER_SUCCESS, user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(Code.GET_USER_FAILED, null);
    }

    public static void delCache(String userCode, String qq) {
        File[] userFolder = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "\\Users\\" + qq).listFiles();
        File[] userCoursesFolder = new File(CourseScheduleQQAdvice.INSTANCE.getDataFolder() + "\\Courses\\" + qq).listFiles();
        boolean flag = false;
        delFileRs(userCode, userFolder, flag);
        delFileRs(userCode, userCoursesFolder, flag);
    }
}