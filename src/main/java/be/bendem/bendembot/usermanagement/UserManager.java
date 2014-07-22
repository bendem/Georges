package be.bendem.bendembot.usermanagement;

import be.bendem.bendembot.IrcClient;
import be.bendem.bendembot.custompackets.KickIrcPacket;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.message.ModeIrcPacket;
import org.apache.commons.lang3.Validate;

/**
 * @author bendem
 */
public class UserManager {

    private final IrcClient bot;

    public UserManager(IrcClient bot) {
        this.bot = bot;
    }

    public void kick(Channel channel, Source user, String message) {
        Validate.isTrue(user.isUser(), "Should be a user");
        channel.getServer().send(new KickIrcPacket(channel, user, message));
    }

    public void ban(Channel channel, Source user, String message) {
        Validate.isTrue(user.isUser(), "Should be a user");
        channel.getServer().send(new ModeIrcPacket(user.getHostName(), "+b", message));
    }


}
