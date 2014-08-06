package be.bendem.bendembot.command.bot;

import be.bendem.bendembot.command.BaseCommand;
import be.bendem.bendembot.command.CommandContext;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Receiver;
import fr.ribesg.alix.api.message.PartIrcPacket;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author bendem
 */
public class ChannelCommand extends BaseCommand {

    public ChannelCommand() {
        super("channel", new String[] {
            "Channel control - Usage: ##.<list|join|leave> [channel]"
        });
    }

    @Override
    protected void exec(CommandContext context, String primaryArgument, List<String> args) {
        String channel = null;
        if(args.size() != 0) {
            if(args.get(0).startsWith("#")) {
                channel = args.get(0);
            } else {
                channel = '#' + args.get(0);
            }
        }

        switch(Action.get(primaryArgument)) {
            case List:
                list(context);
                break;
            case Join:
                join(context, channel);
                break;
            case Leave:
                leave(context, channel);
                break;
        }
    }

    private void list(CommandContext context) {
        if(context.getServer().getChannels().size() == 0) {
            context.error("I'm alone in the dark, please save me :O");
        } else {
            context.message("I'm in " + StringUtils.join(context.getServer().getChannels().stream().map(Receiver::getName).iterator(), ", ") + '.');
        }
    }

    private void join(CommandContext context, String channel) {
        if(channel == null) {
            context.error("No channel specified");
            return;
        }
        if(context.getServer().getChannel(channel) != null) {
            context.error("already in " + channel);
            return;
        }
        context.message("Joining " + channel);
        new Channel(context.getServer(), channel).join();
    }

    private void leave(CommandContext context, String channel) {
        Channel channelToLeave = context.getServer().getChannel(channel);
        if(channelToLeave == null) {
            context.error("channel not found");
            return;
        }
        context.getServer().send(new PartIrcPacket(channelToLeave.getName()));
    }

    private enum Action {
        List, Join, Leave;

        public static Action get(String name) {
            for(Action action : values()) {
                if(action.name().equalsIgnoreCase(name)) {
                    return action;
                }
            }
            return List;
        }
    }
}
