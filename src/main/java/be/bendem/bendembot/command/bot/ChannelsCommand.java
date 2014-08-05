package be.bendem.bendembot.command.bot;

import be.bendem.bendembot.command.BaseCommand;
import be.bendem.bendembot.command.CommandContext;
import fr.ribesg.alix.api.Channel;

import java.util.Collection;
import java.util.List;

/**
 * @author bendem
 */
public class ChannelsCommand extends BaseCommand {

    public ChannelsCommand() {
        super("channels", new String[] {
            "List channels Georges is in"
        }, "chan", "list", "chans");
    }

    @Override
    protected void exec(CommandContext context, String primaryArgument, List<String> args) {
        StringBuilder builder = new StringBuilder("I'm in ");
        context.getServer().getChannels().stream().forEach((channel) -> builder.append(channel.getName()).append(", "));
        context.message(builder.delete(builder.length()-2, builder.length()).append('.').toString());
    }

}
