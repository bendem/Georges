package be.bendem.bendembot.command.messages;

import be.bendem.bendembot.command.BaseCommand;
import be.bendem.bendembot.command.CommandContext;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.enums.Codes;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author bendem
 */
public class MessageCommand extends BaseCommand {

    private final Map<String, String> data;
    private final Set<String> specials;
    private final Map<String, String> messages;

    public MessageCommand(Map<String, String> data, Set<String> specials) {
        super("message", new String[] {
            "Message manipulation - Usage: ##.[set|get|display|delete] <name> [value]"
        });
        this.data = data;
        this.specials = specials;
        this.messages = new HashMap<>();

        specials.add("usernick");
        specials.add("userhost");
        specials.add("username");
        specials.add("servername");
        specials.add("serverhost");
        specials.add("channel");
        specials.add("channelcount");
        specials.add("channelopcount");
        specials.add("channelvoicecount");
        specials.add("channeltopic");
    }

    @Override
    protected void exec(CommandContext context, String primaryArgument, List<String> args) {
        if(args.size() == 0) {
            context.error("Invalid number of arguments");
            return;
        }
        String name = args.get(0).toLowerCase();
        Action action = Action.get(primaryArgument);
        if(action == null) {
            context.error("Unknown action");
            return;
        }

        switch(action) {
            case Display:
                display(name, context);
                break;
            case Get:
                get(name, context);
                break;
            case Set:
                set(name, StringUtils.join(args.listIterator(1), ' '), context);
                break;
            case Delete:
                delete(name, context);
                break;
        }
    }

    private void display(String name, CommandContext context) {
        if(!messages.containsKey(name)) {
            context.error("Message not found");
            return;
        }

        String message = messages.get(name);
        for(Map.Entry<String, String> entry : data.entrySet()) {
            message = message.replace('{' + entry.getKey() + '}', entry.getValue());
        }

        Source user = context.getUser();
        Server server = context.getServer();
        Channel channel = context.getChannel();

        message = message
            .replace("{usernick}", user.getName())
            .replace("{userhost}", user.getHostName())
            .replace("{username}", user.getUserName())
            .replace("{servername}", server.getName())
            .replace("{serverhost}", server.getUrl())
            .replace("{channel}", channel.getName())
            .replace("{channelcount}", String.valueOf(channel.getUsers().size()))
            .replace("{channelopcount}", String.valueOf(channel.getOps().size()))
            .replace("{channelvoicecount}", String.valueOf(channel.getVoiced().size()))
            .replace("{channeltopic}", channel.getTopic() == null ? Codes.ITALIC + "No topic" + Codes.ITALIC : channel.getTopic())
            ;

        context.message(message);
    }

    private void get(String name, CommandContext context) {
        if(!messages.containsKey(name)) {
            context.error("Message not found");
            return;
        }
        context.message(messages.get(name));
    }

    private void set(String name, String value, CommandContext context) {
        messages.put(name, value);
        context.message(name + " message set");
    }

    private void delete(String name, CommandContext context) {
        if(!messages.containsKey(name)) {
            context.error(name + " not found");
            return;
        }
        messages.remove(name);
        context.message(name + " deleted");
    }

    private enum Action {
        Display, Get, Set, Delete;

        public static Action get(String name) {
            for(Action action : values()) {
                if(action.name().equalsIgnoreCase(name) || name == null && action == Display) {
                    return action;
                }
            }
            return null;
        }
    }

}
