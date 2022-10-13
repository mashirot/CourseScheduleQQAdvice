package ski.mashiro;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestSomething {

    @Test
    public void testSth() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            String startTime = "12:00";
            Calendar time = Calendar.getInstance();
            time.setTime(sdf.parse(startTime));
            time.set(Calendar.MINUTE, time.get(Calendar.MINUTE) - 15);
            System.out.println(time.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
