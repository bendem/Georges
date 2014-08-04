package be.bendem.bendembot.command;

import be.bendem.bendembot.IrcClient;
import fr.ribesg.alix.api.message.NickIrcPacket;

import java.util.List;

/**
 * @author bendem
 */
public class NickCommand extends BaseCommand {

    public NickCommand(IrcClient ircClient) {
        super("nick", new String[]{
            "Change bot nick"
        }, true);
    }

    @Override
    protected void exec(String primaryArgument, List<String> args) {
        if(args.size() == 0) {
            error("Not enough arguments");
            return;
        }
        server.send(new NickIrcPacket(args.get(0)));
    }

}
