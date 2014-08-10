package be.bendem.bendembot.automatedmessages;

import be.bendem.bendembot.Context;
import fr.ribesg.alix.api.EventManager;
import fr.ribesg.alix.api.event.ChannelMessageEvent;
import fr.ribesg.alix.api.event.EventHandler;
import fr.ribesg.alix.api.event.UserJoinChannelEvent;
import fr.ribesg.alix.api.event.UserPartChannelEvent;

/**
 * @author bendem
 */
public class MessageEventHandler {

    private final MessageManager manager;

    public MessageEventHandler(MessageManager manager) {
        this.manager = manager;
        EventManager.register(this);
    }

    @EventHandler
    public void onUserJoin(UserJoinChannelEvent e) {
        manager.spawnEvent(Event.UserJoinChannel, new Context(e.getChannel(), e.getUser()));
    }

    @EventHandler
    public void onUserPart(UserPartChannelEvent e) {
        manager.spawnEvent(Event.UserLeaveChannel, new Context(e.getChannel(), e.getUser()));
    }

    public void onUserMessage(ChannelMessageEvent e) {
        manager.spawnEvent(Event.UserMessageChannel, new Context(e.getChannel(), e.getUser()));
    }

    public enum Event {
        UserJoinChannel,
        UserLeaveChannel,
        UserMessageChannel,
        UserGetBanned,
        UserGetKicked,
        UserGetVoiced,
        UserGetOpped,
        UserGetUnbanned,
        UserGetUnopped,
        UserGetUnvoiced
    }
}
