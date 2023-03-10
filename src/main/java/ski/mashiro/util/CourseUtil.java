package ski.mashiro.util;

import ski.mashiro.dto.CourseDto;
import ski.mashiro.file.ConfigFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author MashiroT
 */
public class CourseUtil {
    public static boolean isCourseStart(CourseDto courseDto) {
        int now = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm")));
        int courseStartTime = Integer.parseInt(courseDto.getTime().split(" +- +")[0].replaceAll(":", ""));
        return courseStartTime >= now && courseStartTime - now <= ConfigFile.CONFIG.getLeadTime();
    }

    public static boolean isCourseExpired(CourseDto courseDto) {
        int now = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm")));
        int courseEndTime = Integer.parseInt(courseDto.getTime().split(" +- +")[1].replaceAll(":", ""));
        return courseEndTime <= now;
    }
}
