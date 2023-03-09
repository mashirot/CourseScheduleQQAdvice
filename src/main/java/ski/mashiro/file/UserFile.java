package ski.mashiro.file;

import org.apache.commons.io.FileUtils;
import ski.mashiro.ScheduleQQAdvice;
import ski.mashiro.data.CourseData;
import ski.mashiro.pojo.User;
import ski.mashiro.util.FormatterUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author MashiroT
 */
public class UserFile {
    private static final File FOLDER = ScheduleQQAdvice.INSTANCE.getConfigFolder();
    private static final File USER_FILE = new File(FOLDER, "users.yml");
    public static List<User> USERS;
    public static Map<Long, User> USERS_MAP;

    public static void loadUsers() {
        initFile();
        try {
            USERS = FormatterUtil.transToList(FormatterUtil.YAML_OBJECT_MAPPER.readValue(USER_FILE, Object.class), User.class);
            USERS_MAP = USERS.stream().collect(Collectors.toMap(User::getQq, o -> o));
            initQueue(USERS);
        } catch (IOException e) {
            ScheduleQQAdvice.INSTANCE.getLogger().error("users.yml文件加载失败");
            e.printStackTrace();
        }
    }

    public static void saveUsers() {
        try {
            FileUtils.writeStringToFile(USER_FILE, FormatterUtil.YAML_OBJECT_MAPPER.writeValueAsString(USERS), StandardCharsets.UTF_8);
        } catch (IOException e) {
            ScheduleQQAdvice.INSTANCE.getLogger().error("users.yml文件保存失败");
            e.printStackTrace();
        }
    }

    private static void initFile() {
        if (!USER_FILE.exists()) {
            try {
                if (USER_FILE.createNewFile()) {
                    FileUtils.writeStringToFile(
                            USER_FILE,
                            FormatterUtil.YAML_OBJECT_MAPPER.writeValueAsString(List.of()),
                            StandardCharsets.UTF_8
                    );
                }
            } catch (IOException e) {
                ScheduleQQAdvice.INSTANCE.getLogger().error("users.yml文件创建失败");
                e.printStackTrace();
            }
        }
    }

    private static void initQueue(List<User> list) {
        for (User user : list) {
            CourseData.USER_TODAY_COURSE.putIfAbsent(user.getQq(), new ArrayDeque<>());
            CourseData.refreshQueue(user.getQq());
        }
    }
}
