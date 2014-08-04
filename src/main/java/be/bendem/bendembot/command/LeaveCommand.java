package be.bendem.bendembot.command;

import fr.ribesg.alix.api.Channel;

import java.util.List;

/**
 * @author bendem
 */
public class LeaveCommand extends BaseCommand {

    public LeaveCommand() {
        super("leave", new String[] { "Leave a channel - Usage: ##[.channel]" }, true);
    }

    @Override
    public void exec(String primaryArgument, List<String> args) {
        if(primaryArgument == null && args.size() > 0) {
            primaryArgument = args.get(0);
        } else {
            primaryArgument = channel.getName();
        }

        if(!primaryArgument.startsWith("#")) {
            primaryArgument = "#" + primaryArgument;
        }

        Channel channelToLeave = server.getChannel(primaryArgument);
        if(channelToLeave == null) {
            error("channel not found");
            return;
        }

        // Should be channelToLeave.leave()
        //server.removeChannel(primaryArgument);
        error("I can't leave channels atm");
    }
}
