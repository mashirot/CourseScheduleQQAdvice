package ski.mashiro.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import ski.mashiro.pojo.Course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author MashiroT
 */
public class Utils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String printSchedule(List<Course> courseList) {
        StringBuilder sb = new StringBuilder(Utils.transferDateToStr(new Date()) + "   " + Utils.getWeek() + "\n");
        if (courseList.size() > 0) {
            sb.append("上课时间\t\t").append("上课地点\t\t").append("课程名\n");
            courseList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getCourseDate().split(" ")[1].split("-")[0].split(":")[0])));
            for (Course course : courseList) {
                sb.append(course.getCourseDate().split(" ")[1]).append("\t").append(course.getCourseLocation()).append("\t\t").append(course.getCourseName());
                if (!course.equals(courseList.get(courseList.size() - 1))) {
                    sb.append("\n");
                }
            }
        } else {
            sb.append("今日无课，好好休息");
        }
        return sb.toString();
    }

    public static <T> List<T> transToList(Object data, Class<T> clazz) throws JsonProcessingException {
        CollectionType collectionType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
        String asString = OBJECT_MAPPER.writeValueAsString(data);
        return OBJECT_MAPPER.readValue(asString, collectionType);
    }

    public static Date transferStrToDate(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar now = Calendar.getInstance();
        Calendar rsDate = Calendar.getInstance();
        rsDate.setTime(sdf.parse(strDate));
        rsDate.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        return rsDate.getTime();
    }

    public static String transferDateToStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getWeek() {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1: return "周日";
            case 2: return "周一";
            case 3: return "周二";
            case 4: return "周三";
            case 5: return "周四";
            case 6: return "周五";
            case 7: return "周六";
            default: return null;
        }
    }

}
