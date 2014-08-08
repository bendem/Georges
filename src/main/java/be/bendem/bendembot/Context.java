package be.bendem.bendembot;

import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.enums.Codes;

/**
 * @author bendem
 */
public class Context {

    private final Server  server;
    private final Channel channel;
    private final Source  user;

    public Context(Server server, Channel channel, Source user) {
        this.server = server;
        this.channel = channel;
        this.user = user;
    }

    public Server getServer() {
        return server;
    }

    public Channel getChannel() {
        return channel;
    }

    public Source getUser() {
        return user;
    }

    public void error(String message) {
        error(message, true);
    }

    public void error(String message, boolean includeName) {
        message(Codes.RED + (includeName ? user.getName() + ", " : "") + message, false);
    }

    public void message(String message) {
        message(message, true);
    }

    public void message(String message, boolean includeName) {
        (channel == null ? user : channel).sendMessage((includeName ? user.getName() + ", " : "") + message);
    }

}
