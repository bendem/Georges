package be.bendem.bendembot.custompackets;

import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.enums.Command;
import fr.ribesg.alix.api.message.IrcPacket;
import org.apache.commons.lang3.Validate;

/**
 * @author bendem
 */
public class KickIrcPacket extends IrcPacket {

    public KickIrcPacket(Channel channel, Source source, String reason) {
        super(null, Command.KICK.name(), reason, channel.getName(), source.getName());
        Validate.isTrue(source.isUser(), "Can't kick a server");
    }

}
