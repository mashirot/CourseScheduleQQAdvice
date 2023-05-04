package ski.mashiro.timer;

import net.mamoe.mirai.Bot;
import ski.mashiro.constant.StatusCodeConstants;
import ski.mashiro.data.CourseData;
import ski.mashiro.dto.CourseDto;
import ski.mashiro.dto.Result;
import ski.mashiro.file.ConfigFile;
import ski.mashiro.util.CourseUtil;
import ski.mashiro.util.FormatterUtil;
import ski.mashiro.util.HttpUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author MashiroT
 */
public class Timer {
    public static boolean isHoliday = false;
    public static final ScheduledThreadPoolExecutor POOL = new ScheduledThreadPoolExecutor(3, r -> {
        Thread.currentThread().setName("TimerPool");
        return new Thread(r);
    });

    public static void startTimer() {
        isHoliday();
        LocalDateTime nextDay = LocalDate.now().plusDays(1).atTime(0, 0);
        LocalDateTime nextMinutes = LocalDateTime.now().plus(1, ChronoUnit.MINUTES).with(ChronoField.SECOND_OF_MINUTE, 0);
        courseUpdater(nextDay.plusHours(6).toInstant(ZoneOffset.of("+8")).toEpochMilli() - System.currentTimeMillis());
        nextCourseChecker(nextMinutes.toInstant(ZoneOffset.of("+8")).toEpochMilli() - System.currentTimeMillis());
        holidayChecker(nextDay.toInstant(ZoneOffset.of("+8")).toEpochMilli() - System.currentTimeMillis());
    }

    private static void courseUpdater(long initDelay) {
        POOL.scheduleAtFixedRate(() -> {
            if (isHoliday) {
                return;
            }
            for (Map.Entry<Long, Queue<CourseDto>> queueEntry : CourseData.USER_TODAY_COURSE.entrySet()) {
                queueEntry.getValue().clear();
                CourseData.refreshQueue(queueEntry.getKey());
                if (queueEntry.getValue().isEmpty()) {
                    continue;
                }
                String courseInfo = FormatterUtil.normalFormat(LocalDate.now(), queueEntry.getValue(), "早上好");
                Bot.getInstance(ConfigFile.CONFIG.getBot()).getFriend(queueEntry.getKey()).sendMessage(courseInfo);
            }
        }, initDelay, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
    }

    private static void nextCourseChecker(long initDelay) {
        POOL.scheduleAtFixedRate(() -> {
            if (isHoliday) {
                return;
            }
            for (Map.Entry<Long, Queue<CourseDto>> queueEntry : CourseData.USER_TODAY_COURSE.entrySet()) {
                if (queueEntry.getValue().isEmpty() ||!CourseUtil.isCourseStart(queueEntry.getValue().peek())) {
                    continue;
                }
                String courseInfo = FormatterUtil.normalFormat(LocalDate.now(), List.of(queueEntry.getValue().poll()), null);
                Bot.getInstance(ConfigFile.CONFIG.getBot()).getFriend(queueEntry.getKey()).sendMessage(courseInfo);
            }
        }, initDelay, 60 * 1000, TimeUnit.MILLISECONDS);
    }

    private static void holidayChecker(long initDelay) {
        POOL.scheduleAtFixedRate(Timer::isHoliday, initDelay, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
    }

    private static void isHoliday() {
        Result<Boolean> rs = HttpUtils.sendHolidayReq(LocalDate.now());
        if (rs.getCode() == StatusCodeConstants.HOLIDAY_GET_FAILED) {
            isHoliday = false;
            return;
        }
        isHoliday = rs.getData();
    }
}
