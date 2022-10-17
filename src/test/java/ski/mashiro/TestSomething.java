package ski.mashiro;

import org.junit.Test;
import ski.mashiro.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestSomething {

    @Test
    public void testSth() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String startTime = "12:00";
            Calendar time = Calendar.getInstance();
            time.setTime(sdf.parse(startTime));
            time.set(Calendar.MINUTE, time.get(Calendar.MINUTE) - 15);
            System.out.println(time.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetWeek() {
        String week = Utils.getWeek();
        System.out.println(week);
    }

    @Test
    public void testMorningTime() {
        Calendar now = Calendar.getInstance();
        Calendar morning = Calendar.getInstance();
        if (now.get(Calendar.HOUR_OF_DAY) < 6) {
            morning.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 6, 0, 0);
        } else {
            morning.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH) + 1, 6, 0, 0);
        }
        System.out.println((morning.getTime().getTime() - now.getTime().getTime()) / 1000 / 60 / 60);
    }
}
