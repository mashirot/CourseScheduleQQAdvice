package ski.mashiro.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author MashiroT
 */
public class Utils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String generateSalt(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        char c;
        for (int i = 0; i < length; i++) {
            c = (char) (random.nextInt(94) + 33);
            sb.append(c);
        }
        return sb.toString();
    }

    public static String encryptPassword(String password, String salt) {
        String addSaltPasswd = password.substring(0, password.length() / 2) + salt + password.substring(password.length() / 2);
        return DigestUtils.sha3_256Hex(addSaltPasswd).substring(7,57);
    }

    public static <T> List<T> transToList(Object data, Class<T> clazz) throws JsonProcessingException {
        CollectionType collectionType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
        String asString = OBJECT_MAPPER.writeValueAsString(data);
        return OBJECT_MAPPER.readValue(asString, collectionType);
    }

    public static String transitionDateToStr(Date date) {
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
