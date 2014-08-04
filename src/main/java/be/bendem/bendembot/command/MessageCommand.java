package be.bendem.bendembot.command;

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
    protected void exec(String primaryArgument, List<String> args) {
        if(args.size() == 0) {
            error("Invalid number of arguments");
            return;
        }
        String name = args.get(0).toLowerCase();
        Action action = Action.get(primaryArgument);
        if(action == null) {
            error("Unknown action");
            return;
        }

        switch(action) {
            case Display:
                display(name);
                break;
            case Get:
                get(name);
                break;
            case Set:
                set(name, StringUtils.join(args.listIterator(1), ' '));
                break;
            case Delete:
                delete(name);
                break;
        }
    }

    private void display(String name) {
        if(!messages.containsKey(name)) {
            error("Message not found");
            return;
        }

        String message = messages.get(name);
        for(Map.Entry<String, String> entry : data.entrySet()) {
            message = message.replace('{' + entry.getKey() + '}', entry.getValue());
        }

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

        message(message);
    }

    private void get(String name) {
        if(!messages.containsKey(name)) {
            error("Message not found");
            return;
        }
        message(messages.get(name));
    }

    private void set(String name, String value) {
        messages.put(name, value);
        message(name + " message set");
    }

    private void delete(String name) {
        if(!messages.containsKey(name)) {
            error(name + " not found");
            return;
        }
        messages.remove(name);
        message(name + " deleted");
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
