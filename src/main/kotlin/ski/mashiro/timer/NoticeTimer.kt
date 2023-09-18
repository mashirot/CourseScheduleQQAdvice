package ski.mashiro.timer

import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import ski.mashiro.CourseScheduleQQAdvice
import ski.mashiro.data.CourseData
import ski.mashiro.data.UserData
import ski.mashiro.file.ConfigOp
import ski.mashiro.service.impl.MessageServiceImpl
import ski.mashiro.util.TimeUtils
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.time.Duration

object NoticeTimer {

    private const val UPD_HOUR_OF_DAY = 1
    private const val MORNING_NOTICE_HOUR_OF_DAY = 6
    private val logger = CourseScheduleQQAdvice.logger

    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            async { courseUpdTimer() }
            async { morningNotifyTimer() }
            async(Dispatchers.Default) {
                nextCourseNotifyTimer()
            }
            async { saveCfg() }
        }
    }

    private suspend fun nextCourseNotifyTimer() {
        val startTime = LocalDateTime.now()
        val firstTime = startTime.plusMinutes(1).withSecond(0)
        delay(firstTime.toEpochSecond(ZoneOffset.UTC) - startTime.toEpochSecond(ZoneOffset.UTC))
        while (true) {
            val bot = Bot.findInstance(ConfigOp.config.botQQ) ?: run {
                logger.error("bot: ${ConfigOp.config.botQQ} 不存在")
                delay(Duration.parse("1d"))
                return
            }
            CourseData.courseMap.entries.forEach {
                val friend = bot.getFriend(it.key) ?: run {
                    logger.warning("QQ: ${it.key} 不在bot: ${bot.id} 的好友列表中")
                    return@forEach
                }
                val now = LocalDateTime.now()
                if (!it.value.isEmpty()) {
                    val course = it.value.peek()
                    // 提前20分钟通知
                    if (now.plusMinutes(20).isAfter(TimeUtils.getCourseStartTime(now, course.time))) {
                        friend.sendMessage(MessageServiceImpl.getCourseMsg(it.value.poll()))
                    }
                }
            }
            delay(Duration.parse("1m"))
        }
    }

    private suspend fun courseUpdTimer() {
        var first = true
        do {
            val now = LocalDateTime.now()
            UserData.userMap.values.forEach { user ->
                CourseData.getCourseData(now, user)
            }
            if (first) {
                first = false
                delay(TimeUtils.getInitDelay(UPD_HOUR_OF_DAY))
                continue
            }
            delay(Duration.parse("1d"))
        } while (true)
    }

    private suspend fun morningNotifyTimer() {
        delay(TimeUtils.getInitDelay(MORNING_NOTICE_HOUR_OF_DAY))
        while (true) {
            val bot = Bot.findInstance(ConfigOp.config.botQQ) ?: run {
                logger.error("bot: ${ConfigOp.config.botQQ} 不存在")
                delay(Duration.parse("1d"))
                return
            }
            CourseData.courseMap.entries.forEach {
                val friend = bot.getFriend(it.key) ?: run {
                    logger.warning("QQ: ${it.key} 不在bot: ${bot.id} 的好友列表中")
                    return@forEach
                }
                val msg = MessageServiceImpl.getCoursesWithoutDayOfWeekMsg(it.value.toList())
                friend.sendMessage("早上好\n$msg")
            }
            delay(Duration.parse("1d"))
        }
    }

    private suspend fun saveCfg() {
        while (true) {
            ConfigOp.saveConfig()
            delay(Duration.parse("30m"))
        }
    }

}