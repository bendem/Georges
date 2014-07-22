package be.bendem.bendembot.command;

import be.bendem.bendembot.IrcClient;
import be.bendem.bendembot.custompackets.ActionIrcPacket;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.bot.command.Command;
import fr.ribesg.alix.api.enums.Codes;

/**
 * @author bendem
 */
public class DebugCommand extends Command {

    public DebugCommand(IrcClient ircClient) {
        super(ircClient.getCommandManager(), "debug", null, true, null);
    }

    @Override
    public void exec(Server server, Channel channel, Source user, String primaryArgument, String[] args) {
        try {
            Thread.sleep(500);
        } catch(InterruptedException ignored) {}
        server.send(new ActionIrcPacket(channel.getName(), Codes.GREEN + "sent debug stuff..."));
    }
}
