package be.bendem.bendembot.chathandling;

import be.bendem.bendembot.Context;
import fr.ribesg.alix.api.event.ChannelMessageEvent;
import fr.ribesg.alix.api.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author bendem
 */
public class ChatManager {

    private final List<ChatHandler> chatHandlers;

    public ChatManager() {
        this.chatHandlers = new ArrayList<>();
    }


    @EventHandler
    public void onChannelMessage(final ChannelMessageEvent e) {
        Context context = new Context(e.getChannel(), e.getUser());
        String message = e.getMessage().trim();

        for(ChatHandler handler : chatHandlers) {
            Matcher matcher = handler.matcher(message);
            if(matcher.find()) {
                handler.onChat(context, message, matcher);
            }
        }
    }

    public void register(ChatHandler chatHandler) {
        this.chatHandlers.add(chatHandler);
    }

}
