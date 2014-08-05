package be.bendem.bendembot.command.bot;

import be.bendem.bendembot.command.BaseCommand;
import be.bendem.bendembot.command.CommandContext;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.message.PartIrcPacket;

import java.util.List;

/**
 * @author bendem
 */
public class LeaveCommand extends BaseCommand {

    public LeaveCommand() {
        super("leave", new String[] { "Leave a channel - Usage: ##[.channel]" }, true, "part");
    }

    @Override
    public void exec(CommandContext context, String primaryArgument, List<String> args) {
        if(primaryArgument == null && args.size() > 0) {
            primaryArgument = args.get(0);
        } else {
            primaryArgument = context.getChannel().getName();
        }
        if(!primaryArgument.startsWith("#")) {
            primaryArgument = "#" + primaryArgument;
        }

        Channel channelToLeave = context.getServer().getChannel(primaryArgument);
        if(channelToLeave == null) {
            context.error("channel not found");
            return;
        }
        // Should be channelToLeave.part()
        context.getServer().send(new PartIrcPacket(channelToLeave.getName()));
    }

}
