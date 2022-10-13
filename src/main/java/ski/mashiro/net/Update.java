package ski.mashiro.net;

import ski.mashiro.CourseScheduleQQAdvice;
import ski.mashiro.config.Config;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author MashiroT
 */
public class Update {

    public static void checkUpdate() {
        try {
            URL url = new URL("https://update.check.mashiro.ski/CourseScheduleQQAdvice.txt");
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            String lastedVersion = br.readLine();
            if (!Config.CONFIGURATION.getVersion().equals(lastedVersion)) {
                CourseScheduleQQAdvice.INSTANCE.getLogger().info("插件有新版本,当前版本: " + Config.CONFIGURATION.getVersion() + " , 最新版本: " + lastedVersion);
            } else {
                CourseScheduleQQAdvice.INSTANCE.getLogger().info("已是最新版本");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
