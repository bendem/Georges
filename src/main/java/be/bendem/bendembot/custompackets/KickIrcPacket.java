package be.bendem.bendembot.custompackets;

import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.enums.Command;
import fr.ribesg.alix.api.message.IrcPacket;

/**
 * @author bendem
 */
public class KickIrcPacket extends IrcPacket {

    public KickIrcPacket(Channel channel, Source source, String reason) {
        this(channel, source.getName(), reason);
    }

    public KickIrcPacket(Channel channel, String target, String reason) {
        super(null, Command.KICK.name(), reason, channel.getName(), target);
    }

}
