package be.bendem.bendembot.utils;

import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.message.ModeIrcPacket;

/**
 * @author bendem
 */
public class ModeUtils {

    public static void op(Server server, Channel channel, String target) {
        send(server, channel, "+o", target);
    }

    public static void ban(Server server, Channel channel, String target) {
        send(server, channel, "+b", target);
    }

    public static void voice(Server server, Channel channel, String target) {
        send(server, channel, "+v", target);
    }

    private static void send(Server server, Channel channel, String mode, String target) {
        server.send(new ModeIrcPacket(channel.getName(), mode, target));
    }

}
