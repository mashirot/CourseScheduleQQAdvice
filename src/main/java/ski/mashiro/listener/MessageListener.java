package ski.mashiro.listener;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import org.jetbrains.annotations.NotNull;
import ski.mashiro.config.Config;

import java.util.Arrays;

/**
 * @author MashiroT
 */
public class MessageListener extends SimpleListenerHost {

    @EventHandler
    public void listenFriendMsg(@NotNull FriendMessageEvent friendMessageEvent) {
        if (Arrays.asList(Config.WHITELIST.getWhitelist()).contains(friendMessageEvent.getSender().getId() + "")) {
            if (friendMessageEvent.getMessage().toString().contains("早")) {
                friendMessageEvent.getSubject().sendMessage("早早早");
            }
            if (friendMessageEvent.getMessage().toString().contains("晚安")) {
                friendMessageEvent.getSubject().sendMessage("晚安晚安");
            }
        }
    }

}
