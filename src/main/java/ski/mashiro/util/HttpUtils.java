package ski.mashiro.util;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ski.mashiro.dto.CourseDto;
import ski.mashiro.dto.CourseSearchDto;
import ski.mashiro.dto.Result;
import ski.mashiro.file.ConfigFile;
import ski.mashiro.pojo.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ski.mashiro.constant.StatusCodeConstants.*;

/**
 * @author MashiroT
 */
public class HttpUtils {

    public static Result<List<CourseDto>> sendCourseReq(CourseSearchDto courseSearchDto) {
        try {
            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(ConfigFile.CONFIG.getUrl() + "/api/sel")
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(FormatterUtil.JSON_OBJECT_MAPPER.writeValueAsString(courseSearchDto).getBytes()))
                    .build();
            try (Response resp = httpClient.newCall(request).execute()) {
                assert resp.body() != null;
                Result<List<CourseDto>> data = FormatterUtil.JSON_OBJECT_MAPPER.readValue(resp.body().string(), new TypeReference<Result<List<CourseDto>>>() {
                });
                if (data.getCode() == COURSE_LIST_FAILED) {
                    return Result.failed(COURSE_LIST_SUCCESS, null);
                }
                return Result.success(COURSE_LIST_SUCCESS, data.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(500, "Server Err");
        }
    }

    public static Result<User> sendUserReq(User user) {
        try {
            OkHttpClient httpClient = new OkHttpClient();
            Request req = new Request.Builder()
                    .url(ConfigFile.CONFIG.getUrl() + "/api/login")
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(FormatterUtil.YAML_OBJECT_MAPPER.writeValueAsString(user).getBytes()))
                    .build();
            try (Response resp = httpClient.newCall(req).execute()) {
                assert resp.body() != null;
                Result<User> data = FormatterUtil.JSON_OBJECT_MAPPER.readValue(resp.body().string(), new TypeReference<Result<User>>() {
                });
                if (data.getCode() == USER_LOGIN_FAILED) {
                    return Result.failed(USER_LOGIN_FAILED, null);
                }
                return Result.success(USER_LOGIN_SUCCESS, (User) data.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(500, "Server Err");
        }
    }

    public static Result<Boolean> sendHolidayReq(LocalDate localDate) {
        OkHttpClient httpClient = new OkHttpClient();
        Request req = new Request.Builder()
//                https://api.apihubs.cn/holiday/get?date=20230504&cn=1&size=1
                .url("https://api.apihubs.cn/holiday/get?date=" + localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "&cn=1&size=1")
                .build();
        try (Response resp = httpClient.newCall(req).execute()) {
            assert resp.body() != null;
            String respJson = resp.body().string();
//            "holiday_legal_cn":"非法定节假日","holiday_recess_cn":"非假期节假日"
//            "holiday_legal_cn":"法定节假日","holiday_recess_cn":"假期节假日"
            boolean isLegalHoliday = "法定节假日".equals(respJson.substring(respJson.indexOf("\"holiday_legal_cn\":\"") + 20, respJson.indexOf("\",\"holiday_recess_cn\"")));
            return Result.success(HOLIDAY_GET_SUCCESS, isLegalHoliday);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed(HOLIDAY_GET_FAILED, null);
        }
    }
}
