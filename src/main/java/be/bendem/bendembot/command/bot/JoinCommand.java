package be.bendem.bendembot.command.bot;

import be.bendem.bendembot.command.BaseCommand;
import be.bendem.bendembot.command.CommandContext;

import java.util.List;

/**
 * @author bendem
 */
public class JoinCommand extends BaseCommand {

    public JoinCommand() {
        super("join", new String[] { "Join a new channel - Usage: ##.channel" }, true);
    }

    @Override
    public void exec(CommandContext context, String primaryArgument, List<String> args) {
        if(primaryArgument == null) {
            return;
        }

        if(!primaryArgument.startsWith("#")) {
            primaryArgument = "#" + primaryArgument;
        }

        if(context.getServer().getChannel(primaryArgument) != null) {
            context.error("already in " + primaryArgument);
            return;
        }

        context.getServer().addChannel(primaryArgument);
        context.getServer().joinChannels();
    }
}
