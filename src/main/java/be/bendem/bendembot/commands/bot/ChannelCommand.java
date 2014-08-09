package be.bendem.bendembot.commands.bot;

import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.Context;
import be.bendem.bendembot.utils.EnumUtils;
import be.bendem.bendembot.utils.NickUtils;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Receiver;
import fr.ribesg.alix.api.message.PartIrcPacket;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author bendem
 */
public class ChannelCommand extends BaseCommand {

    public ChannelCommand() {
        super("channel", new String[] {
            "Channel control - Usage: ##.<" + EnumUtils.joinValues(Action.class, "|") + "> [channel] [more]"
        }, "c");
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        String channel = null;
        if(args.size() != 0) {
            if(args.get(0).startsWith("#")) {
                channel = args.get(0);
            } else {
                channel = '#' + args.get(0);
            }
        } else if(context.getChannel() != null) {
            channel = context.getChannel().getName();
        }

        switch(EnumUtils.getIgnoreCase(Action.class, primaryArgument, Action.List)) {
            case List:
                list(context);
                break;
            case Join:
                join(context, channel);
                break;
            case Leave:
                leave(context, channel);
                break;
            case Users:
                users(context, channel, args.size() > 1 && args.get(1).equalsIgnoreCase("more"));
                break;
        }
    }

    private void list(Context context) {
        if(context.getServer().getChannels().size() == 0) {
            context.error("I'm alone in the dark, please save me :O");
        } else {
            context.message("I'm in " + StringUtils.join(context.getServer().getChannels().stream().map(Receiver::getName).iterator(), ", ") + '.');
        }
    }

    private void join(Context context, String channel) {
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

    private void leave(Context context, String channel) {
        if(channel == null) {
            context.error("You're not in a channel, tell me which one I should leave");
            return;
        }
        Channel channelToLeave = context.getServer().getChannel(channel);
        if(channelToLeave == null) {
            context.error("channel not found");
            return;
        }
        context.getServer().send(new PartIrcPacket(channelToLeave.getName()));
    }

    private void users(Context context, String channel, boolean more) {
        if(channel == null) {
            context.error("No channel provided :(");
            return;
        }
        Channel channelToList = context.getServer().getChannel(channel);
        if(channelToList == null) {
            context.error("I'm not in this channel :'(");
            return;
        }
        Set<String> users = channelToList.getUsers();
        if(users.isEmpty()) {
            context.error("No user? Really?");
            return;
        }
        Iterator<String> sorted = users.stream().sorted((user1, user2) -> {
            Permission permA = Permission.fromChar(user1.charAt(0));
            Permission permB = Permission.fromChar(user2.charAt(0));
            if(permA.ordinal() > permB.ordinal())
                return 1;
            if(permA.ordinal() < permB.ordinal())
                return -1;
            return user1.compareTo(user2);
        }).map(NickUtils::antiHighlight).iterator();

        // TODO Filter if too much users
        StringBuilder message = new StringBuilder("There are ")
            .append(users.size())
            .append(" users in ")
            .append(channel)
            .append(" (")
            .append(channelToList.getOps().size())
            .append(" ops and ")
            .append(channelToList.getVoiced().size())
            .append(" voiced)");

        if(more) {
            message.append(": ").append(StringUtils.join(sorted, ", "));
        }
        context.message(message.append('.').toString());
    }

    private enum Permission {
        Op, Voice, User;

        public static Permission fromChar(char c) {
            switch(c) {
                case '@': return Op;
                case '+': return Voice;
                default:  return User;
            }
        }
    }

    private enum Action {
        List, Join, Leave, Users
    }

}
