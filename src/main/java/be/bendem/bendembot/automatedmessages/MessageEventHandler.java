package be.bendem.bendembot.automatedmessages;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.usermanagement.UserManager;
import fr.ribesg.alix.api.EventManager;
import fr.ribesg.alix.api.event.ChannelMessageEvent;
import fr.ribesg.alix.api.event.EventHandler;
import fr.ribesg.alix.api.event.EventHandlerPriority;
import fr.ribesg.alix.api.event.ReceivedPacketEvent;
import fr.ribesg.alix.api.event.UserJoinChannelEvent;
import fr.ribesg.alix.api.event.UserKickedFromChannelEvent;
import fr.ribesg.alix.api.event.UserPartChannelEvent;
import fr.ribesg.alix.api.event.UserQuitServerEvent;
import fr.ribesg.alix.api.message.ModeIrcPacket;
import fr.ribesg.alix.api.message.PrivMsgIrcPacket;
import org.apache.commons.lang3.StringUtils;

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
    @EventHandler(priority = EventHandlerPriority.HIGH, ignoreConsumed = false)
    public void onUserJoin(UserJoinChannelEvent e) {
        Context context = new Context(e.getChannel(), e.getUser());
        if(!userManager.isKnown(e.getUser().getName())) {
            messageManager.spawnEvent(Event.UserFirstJoin, context);
        }
        messageManager.spawnEvent(Event.UserJoin, context);
    }

    @EventHandler(ignoreConsumed = false)
    public void onUserPart(UserPartChannelEvent e) {
        messageManager.spawnEvent(Event.UserLeave, new Context(e.getChannel(), e.getUser()));
    }

    @EventHandler(ignoreConsumed = false)
    public void onUserQuit(UserQuitServerEvent e) {
        e.getServer().getChannels().stream()
            .filter(channel -> channel.getUserNicknames().contains(e.getUser().getName()))
            .forEach(channel -> messageManager.spawnEvent(Event.UserLeave, new Context(channel, e.getUser())));
    }

    @EventHandler(ignoreConsumed = false)
    public void onUserMessage(ChannelMessageEvent e) {
        messageManager.spawnEvent(Event.UserMessage, new Context(e.getChannel(), e.getUser()));
    }

    @EventHandler(ignoreConsumed = false)
    public void onUserKick(UserKickedFromChannelEvent e) {
        messageManager.spawnEvent(Event.UserKicked, new Context(e.getChannel(), e.getUser()));
    }

    @EventHandler(ignoreConsumed = false)
    public void onMode(ReceivedPacketEvent e) {
        if(!(e.getPacket() instanceof ModeIrcPacket)) {
            return;
        }

        // TODO
        ModeIrcPacket packet = (ModeIrcPacket) e.getPacket();
        PrivMsgIrcPacket msgPacket = new PrivMsgIrcPacket("#Georges", packet.getEntityName() + " / " + StringUtils.join(packet.getModeParameters()));
        e.getSource().send(msgPacket);
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
