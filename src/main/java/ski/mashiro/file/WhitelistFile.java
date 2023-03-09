package ski.mashiro.file;

import org.apache.commons.io.FileUtils;
import ski.mashiro.ScheduleQQAdvice;
import ski.mashiro.util.FormatterUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MashiroT
 */
public class WhitelistFile {
    private static final File FOLDER = ScheduleQQAdvice.INSTANCE.getConfigFolder();
    private static final File WHITELIST_FILE = new File(FOLDER, "whitelist.yml");
    public static List<Long> WHITELIST;

    public static void loadWhitelist() {
        initFile();
        try {
            WHITELIST = FormatterUtil.transToList(FormatterUtil.YAML_OBJECT_MAPPER.readValue(WHITELIST_FILE, Object.class), Long.class);
        } catch (IOException e) {
            ScheduleQQAdvice.INSTANCE.getLogger().error("whitelist.yml文件加载失败");
            e.printStackTrace();
        }
    }

    public static void saveWhitelist() {
        try {
            FileUtils.writeStringToFile(WHITELIST_FILE, FormatterUtil.YAML_OBJECT_MAPPER.writeValueAsString(WHITELIST), StandardCharsets.UTF_8);
        } catch (IOException e) {
            ScheduleQQAdvice.INSTANCE.getLogger().error("whitelist.yml文件保存失败");
            e.printStackTrace();
        }
    }

    private static void initFile() {
        if (!WHITELIST_FILE.exists()) {
            try {
                if (WHITELIST_FILE.createNewFile()) {
                    FileUtils.writeStringToFile(WHITELIST_FILE, FormatterUtil.YAML_OBJECT_MAPPER.writeValueAsString(List.of(12345L)), StandardCharsets.UTF_8);
                }
            } catch (IOException e) {
                ScheduleQQAdvice.INSTANCE.getLogger().error("whitelist.yml文件创建失败");
                e.printStackTrace();
            }
        }
    }
}
