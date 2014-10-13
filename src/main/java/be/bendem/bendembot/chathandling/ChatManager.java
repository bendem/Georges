package be.bendem.bendembot.chathandling;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.Georges;
import fr.ribesg.alix.api.EventManager;
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
        EventManager.register(this);
    }


    @EventHandler
    public void onChannelMessage(final ChannelMessageEvent event) {
        Context context = new Context(event.getChannel(), event.getUser());
        String message = event.getMessage().trim();

        for(ChatHandler handler : chatHandlers) {
            Matcher matcher = handler.matcher(message);
            if(matcher.find()) {
                try {
                    handler.onChat(context, message, matcher);
                } catch(Exception e) {
                    context.error(e.getClass().getSimpleName() + (e.getMessage() == null ? "" : e.getMessage()));
                    Georges.getLogger().error(e);
                }
            }
        }
    }

    public void register(ChatHandler chatHandler) {
        this.chatHandlers.add(chatHandler);
    }

}
