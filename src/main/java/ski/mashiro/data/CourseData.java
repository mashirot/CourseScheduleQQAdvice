package ski.mashiro.data;

import ski.mashiro.constant.StatusCodeConstants;
import ski.mashiro.dto.CourseDto;
import ski.mashiro.dto.CourseSearchDto;
import ski.mashiro.dto.Result;
import ski.mashiro.file.UserFile;
import ski.mashiro.pojo.User;
import ski.mashiro.util.CourseUtil;
import ski.mashiro.util.FormatterUtil;
import ski.mashiro.util.HttpUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author MashiroT
 */
public class CourseData {

    public static final Map<Long, Queue<CourseDto>> USER_TODAY_COURSE = new HashMap<>(UserFile.USERS.size());

    public static List<CourseDto> getCourse(CourseSearchDto searchDto) {
        Result<List<CourseDto>> result = HttpUtils.sendCourseReq(searchDto);
        if (result.getCode() == StatusCodeConstants.COURSE_LIST_FAILED) {
            return null;
        }
        return FormatterUtil.sortByTime(result.getData());
    }

    public static boolean refreshQueue(long qq) {
        User currUser = UserFile.USERS_MAP.get(qq);
        CourseSearchDto searchDto = new CourseSearchDto(currUser.getUid(), FormatterUtil.INT_DAY_TO_STR_MAP.get(LocalDate.now().getDayOfWeek().getValue()), currUser.getTermStartDate());
        List<CourseDto> courseDtoList = CourseData.getCourse(searchDto);
        if (courseDtoList == null) {
            return false;
        }
        CourseData.USER_TODAY_COURSE.get(qq).addAll(courseDtoList);
        while (!CourseData.USER_TODAY_COURSE.get(qq).isEmpty() && CourseUtil.isCourseExpired(CourseData.USER_TODAY_COURSE.get(qq).peek())) {
            CourseData.USER_TODAY_COURSE.get(qq).poll();
        }
        return true;
    }
}
