package be.bendem.bendembot.automatedmessages;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.usermanagement.UserManager;
import fr.ribesg.alix.api.EventManager;
import fr.ribesg.alix.api.event.ChannelMessageEvent;
import fr.ribesg.alix.api.event.EventHandler;
import fr.ribesg.alix.api.event.EventHandlerPriority;
import fr.ribesg.alix.api.event.UserJoinChannelEvent;
import fr.ribesg.alix.api.event.UserPartChannelEvent;

/**
 * @author bendem
 */
public class MessageEventHandler {

    private final MessageManager messageManager;
    private final UserManager userManager;

    public MessageEventHandler(MessageManager messageManager, UserManager userManager) {
        this.messageManager = messageManager;
        this.userManager = userManager;
        EventManager.register(this);
    }

    // Maybe the user manager should do that when adding a new user
    @EventHandler(priority = EventHandlerPriority.HIGH)
    public void onUserJoin(UserJoinChannelEvent e) {
        Context context = new Context(e.getChannel(), e.getUser());
        if(!userManager.isKnown(e.getUser().getName())) {
            messageManager.spawnEvent(Event.UserFirstJoin, context);
        }
        messageManager.spawnEvent(Event.UserJoin, context);
    }

    @EventHandler
    public void onUserPart(UserPartChannelEvent e) {
        messageManager.spawnEvent(Event.UserLeave, new Context(e.getChannel(), e.getUser()));
    }

    public void onUserMessage(ChannelMessageEvent e) {
        messageManager.spawnEvent(Event.UserMessage, new Context(e.getChannel(), e.getUser()));
    }

    public enum Event {
        UserFirstJoin,
        UserJoin,
        UserLeave,
        UserMessage,
        UserBanned,
        UserKicked,
        UserVoiced,
        UserOpped,
        UserUnbanned,
        UserUnopped,
        UserUnvoiced
    }
}
