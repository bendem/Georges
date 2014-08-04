package be.bendem.bendembot.command;

import fr.ribesg.alix.api.enums.Codes;

import java.util.List;

/**
 * @author bendem
 */
public class JoinCommand extends BaseCommand {

    public JoinCommand() {
        super("join", new String[] { "Join a new channel - Usage: ##.channel" }, true);
    }

    @Override
    public void exec(String primaryArgument, List<String> args) {
        if(primaryArgument == null) {
            return;
        }

        if(!primaryArgument.startsWith("#")) {
            primaryArgument = "#" + primaryArgument;
        }

        if(server.getChannel(primaryArgument) != null) {
            channel.sendMessage(Codes.RED + user.getName() + ", already in " + primaryArgument);
            return;
        }

        server.addChannel(primaryArgument);
        server.joinChannels();
    }
}
