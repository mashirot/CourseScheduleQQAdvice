package ski.mashiro.net;

import org.junit.Test;
import ski.mashiro.pojo.Result;
import ski.mashiro.pojo.User;

public class TestHttpRequest {

    @Test
    public void testGetSchedule() {
        Result schedule = HttpRequest.getEffSchedule(new User("221003710519", "3f2f4705cdadfa6723297b07f4e67020"));
        System.out.println(schedule);
    }
}
