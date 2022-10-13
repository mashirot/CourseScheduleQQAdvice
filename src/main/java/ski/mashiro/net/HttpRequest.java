package ski.mashiro.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import ski.mashiro.config.Config;
import ski.mashiro.pojo.Code;
import ski.mashiro.pojo.Result;
import ski.mashiro.pojo.User;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author MashiroT
 */
public class HttpRequest {

    private static final String URL = Config.CONFIGURATION.getApiUrl();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static Result getSchedule(User user) {
        try {
            URL reqUrl = new URL(URL + "/schedules");
            return sendPost(user, reqUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(Code.HTTP_REQUEST_ERR, null);
    }
    public static Result getEffSchedule(User user) {
        try {
            URL reqUrl = new URL(URL + "/effSchedules");
            return sendPost(user, reqUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(Code.HTTP_REQUEST_ERR, null);
    }

    public static Result getTodaySchedule(User user) {
        try {
            URL reqUrl = new URL(URL + "/dateSchedules");
            return sendPost(user, reqUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(Code.HTTP_REQUEST_ERR, null);
    }

    public static Result getTodayEffSchedule(User user) {
        try {
            URL reqUrl = new URL(URL + "/effDateSchedules");
            return sendPost(user, reqUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(Code.HTTP_REQUEST_ERR, null);
    }

    @NotNull
    private static Result sendPost(User user, URL reqUrl) throws IOException {
        StringBuilder sb = new StringBuilder();
        HttpsURLConnection urlConnection = (HttpsURLConnection) reqUrl.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:105.0) Gecko/20100101 Firefox/105.0");
        urlConnection.setRequestProperty("Connection", "keep-alive");
        urlConnection.setRequestProperty("Accept", "*/*");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestMethod("POST");
        urlConnection.setInstanceFollowRedirects(true);
        urlConnection.setDoOutput(true);
        try (
                OutputStream os = urlConnection.getOutputStream()
        ) {
            os.write(OBJECT_MAPPER.writeValueAsBytes(user));
            os.flush();
        }
        if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (
                BufferedReader bf = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))
            ) {
                String len;
                while ((len = bf.readLine()) != null) {
                    sb.append(len);
                }
                Result result = OBJECT_MAPPER.readValue(sb.toString(), Result.class);
                return new Result(result.getCode(), result.getData());
            }
        }
        return new Result(urlConnection.getResponseCode(), null);
    }

}
