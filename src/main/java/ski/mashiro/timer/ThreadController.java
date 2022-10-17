package ski.mashiro.timer;

import com.fasterxml.jackson.databind.ObjectMapper;
import ski.mashiro.CourseScheduleQQAdvice;

import java.util.*;
import java.util.concurrent.*;


/**
 * @author MashiroT
 */
public class ThreadController {
    private static int i = 0;
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
            CourseScheduleQQAdvice.INSTANCE.getLogger().info("现在的时间是：" + new Date() + " 进入MorningThread调用线程 " + i + "次");
            Thread thread = new Thread(new MorningThread());
            thread.start();
        }, Math.abs(morning.getTime().getTime() - now.getTime().getTime()), 1, TimeUnit.DAYS);
        pool.scheduleAtFixedRate(() -> {
            if (i == 0) {
                i++;
                return;
            }
            Thread thread = new Thread(new RemindThread());
            thread.start();
        }, 0, 1, TimeUnit.MINUTES);
    }
}
