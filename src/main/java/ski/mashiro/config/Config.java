package ski.mashiro.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;
import ski.mashiro.CourseScheduleQQAdvice;
import ski.mashiro.pojo.Configuration;
import ski.mashiro.pojo.Whitelist;

import java.io.File;

/**
 * @author MashiroT
 */
public class Config {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());
    private static final File CONFIG_FILE = new File(CourseScheduleQQAdvice.INSTANCE.getConfigFolder() + "\\Config.yml");
    private static final File WHITELIST_FILE = new File(CourseScheduleQQAdvice.INSTANCE.getConfigFolder() + "\\Whitelist.yml");

    public static Configuration CONFIGURATION = null;
    public static Whitelist WHITELIST = null;

    public static void createConfig() {
        try {
            if (CONFIG_FILE.createNewFile()) {
                FileUtils.write(CONFIG_FILE, OBJECT_MAPPER.writeValueAsString(new Configuration()), "utf-8");
            }
            if (WHITELIST_FILE.createNewFile()) {
                FileUtils.write(WHITELIST_FILE, OBJECT_MAPPER.writeValueAsString(new Whitelist()), "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() {
        if (!CONFIG_FILE.exists() || !WHITELIST_FILE.exists()) {
            createConfig();
        }
        try {
            CONFIGURATION = OBJECT_MAPPER.readValue(FileUtils.readFileToString(CONFIG_FILE, "utf-8"), Configuration.class);
            WHITELIST = OBJECT_MAPPER.readValue(FileUtils.readFileToString(WHITELIST_FILE, "utf-8"), Whitelist.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            FileUtils.write(CONFIG_FILE, OBJECT_MAPPER.writeValueAsString(CONFIGURATION), "utf-8");
            FileUtils.write(WHITELIST_FILE, OBJECT_MAPPER.writeValueAsString(WHITELIST), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
