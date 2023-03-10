package ski.mashiro.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import ski.mashiro.dto.CourseDto;
import ski.mashiro.file.ConfigFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author MashiroT
 */
public class FormatterUtil {
    public static final ObjectMapper JSON_OBJECT_MAPPER = new ObjectMapper();
    public static final ObjectMapper YAML_OBJECT_MAPPER = new ObjectMapper(new JsonFactory());
    private static final Map<String, Integer> STR_DAY_TO_INT_MAP = Map.of("Monday", 1, "Tuesday", 2, "Wednesday", 3, "Thursday", 4, "Friday", 5, "Saturday", 6, "Sunday", 7);
    public static final Map<Integer, String> INT_DAY_TO_STR_MAP = Map.of(1, "Monday", 2, "Tuesday", 3, "Wednesday", 4, "Thursday", 5, "Friday", 6, "Saturday", 7, "Sunday");

    public static <T> List<T> transToList(Object data, Class<T> clazz) throws JsonProcessingException {
        CollectionType collectionType = JSON_OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
        String asString = JSON_OBJECT_MAPPER.writeValueAsString(data);
        return JSON_OBJECT_MAPPER.readValue(asString, collectionType);
    }

    public static String normalFormat(Collection<CourseDto> courseDtoList, String msg) {
        if (courseDtoList == null) {
            return "Server Err";
        }
        if (courseDtoList.size() == 0) {
            return "暂无课程";
        }
        StringBuilder sb = new StringBuilder();
        if (msg != null) {
            sb.append(msg).append("\n");
        }
        sb.append(getDateTitleInfo()).append("\n");
        sb.append("上课时间\t\t上课地点\t\t课程名\n");
        Iterator<CourseDto> it = courseDtoList.iterator();
        while (it.hasNext()) {
            CourseDto courseDto = it.next();
            sb.append(courseDto.getTime()).append("\t").append(courseDto.getPlace()).append("\t").append(courseDto.getName());
            if (it.hasNext()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static String getDateTitleInfo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\t" + FormatterUtil.INT_DAY_TO_STR_MAP.get(LocalDate.now().getDayOfWeek().getValue());
    }

    public static String detailFormat(List<CourseDto> courseDtoList) {
        if (courseDtoList == null) {
            return "Server Err";
        }
        if (courseDtoList.size() == 0) {
            return "暂无课程";
        }
        StringBuilder sb = new StringBuilder("日期\t\t上课时间\t\t上课地点\t\t课程名\n");
        for (int i = 0; i < courseDtoList.size(); i++) {
            CourseDto courseDto = courseDtoList.get(i);
            sb.append(courseDto.getDayOfWeek()).append("\t").append(courseDto.getTime()).append("\t").append(courseDto.getPlace()).append("\t").append(courseDto.getName());
            if (i + 1 < courseDtoList.size()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static List<CourseDto> sortByTime(List<CourseDto> courseDtoList) {
        if (courseDtoList == null) {
            return null;
        }
        courseDtoList.sort(((o1, o2) -> {
            if (!o1.getDayOfWeek().equals(o2.getDayOfWeek())) {
                return STR_DAY_TO_INT_MAP.get(o1.getDayOfWeek()) - STR_DAY_TO_INT_MAP.get(o2.getDayOfWeek());
            }
            int o1Time = Integer.parseInt(o1.getTime().split(" +- +")[0].replaceAll(":", ""));
            int o2Time = Integer.parseInt(o2.getTime().split(" +- +")[0].replaceAll(":", ""));
            return o1Time - o2Time;
        }));
        return courseDtoList;
    }


}
