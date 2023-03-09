package ski.mashiro.file;

import org.apache.commons.io.FileUtils;
import ski.mashiro.ScheduleQQAdvice;
import ski.mashiro.pojo.Config;
import ski.mashiro.util.FormatterUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author MashiroT
 */
public class ConfigFile {
    private static final File FOLDER = ScheduleQQAdvice.INSTANCE.getConfigFolder();
    private static final File CONFIG_FILE = new File(FOLDER, "config.yml");
    public static Config CONFIG;

    public static void loadConfig() {
        initFile();
        try {
            CONFIG = FormatterUtil.YAML_OBJECT_MAPPER.readValue(CONFIG_FILE, Config.class);
        } catch (IOException e) {
            ScheduleQQAdvice.INSTANCE.getLogger().error("config.yml文件加载失败");
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            FileUtils.writeStringToFile(CONFIG_FILE, FormatterUtil.YAML_OBJECT_MAPPER.writeValueAsString(CONFIG), StandardCharsets.UTF_8);
        } catch (IOException e) {
            ScheduleQQAdvice.INSTANCE.getLogger().error("config.yml文件保存失败");
            e.printStackTrace();
        }
    }

    private static void initFile() {
        if (!CONFIG_FILE.exists()) {
            try {
                if (CONFIG_FILE.createNewFile()) {
                    FileUtils.writeStringToFile(
                            CONFIG_FILE,
                            FormatterUtil.YAML_OBJECT_MAPPER.writeValueAsString(
                                    new Config("http://127.0.0.1:8080", 20, 12345L, 12345L)
                            ),
                            StandardCharsets.UTF_8
                    );
                }
            } catch (IOException e) {
                ScheduleQQAdvice.INSTANCE.getLogger().error("config.yml文件创建失败");
                e.printStackTrace();
            }
        }
    }
}
