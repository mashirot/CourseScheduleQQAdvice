package ski.mashiro.timer;

import com.fasterxml.jackson.databind.ObjectMapper;
import ski.mashiro.CourseScheduleQQAdvice;

import java.util.*;
import java.util.concurrent.*;


/**
 * @author MashiroT
 */
public class ThreadController {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void openTasks() {
        CourseScheduleQQAdvice.INSTANCE.getLogger().info("进入ThreadController");
        Calendar now = Calendar.getInstance();
        Calendar morning = Calendar.getInstance();
        if (now.get(Calendar.HOUR_OF_DAY) < 6) {
            morning.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 6, 0, 0);
        } else {
            morning.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH) + 1, 6, 0, 0);
        }
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        pool.scheduleAtFixedRate(() -> {
            CourseScheduleQQAdvice.INSTANCE.getLogger().info("现在的时间是：" + new Date() + " 进入MorningThread调用线程");
            Thread thread = new Thread(new MorningThread());
            thread.start();
        }, Math.abs(morning.getTime().getTime() - now.getTime().getTime()), 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);

        pool.scheduleAtFixedRate(() -> {
            Thread thread = new Thread(new RemindThread());
            thread.start();
        }, 1, 1, TimeUnit.MINUTES);
    }
}
