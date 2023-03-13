package ski.mashiro.util;

import ski.mashiro.dto.CourseDto;
import ski.mashiro.file.ConfigFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * @author MashiroT
 */
public class CourseUtil {
    public static boolean isCourseStart(CourseDto courseDto) {
        Calendar now = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        String[] times = courseDto.getTime().split(" +- +")[0].split(":");
        startDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
        startDate.set(Calendar.MINUTE, Integer.parseInt(times[1]));
        startDate.set(Calendar.SECOND, 0);
        return startDate.after(now) && startDate.getTimeInMillis() - now.getTimeInMillis() <= ConfigFile.CONFIG.getLeadTime() * 60 * 1000;
    }

    public static boolean isCourseExpired(CourseDto courseDto) {
        int now = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm")));
        int courseStartTime = Integer.parseInt(courseDto.getTime().split(" +- +")[0].replaceAll(":", ""));
        return courseStartTime <= now;
    }
}
