package be.bendem.bendembot.commands.messages;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.automatedmessages.Message;
import be.bendem.bendembot.automatedmessages.MessageManager;
import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.utils.EnumUtils;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.enums.Codes;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bendem
 */
public class MessageCommand extends BaseCommand {


    private final MessageManager manager;

    public MessageCommand(MessageManager manager) {
        super("message", new String[] {
            "Message manipulation - Usage: ##.<" + EnumUtils.joinValues(Action.class, "|") + "> <name> [value(s)]",
            "Events can be used with data checks (but I don't like writing doc so guess them :/)"
        }, true, "mes");
        this.manager = manager;
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        if(args.size() == 0) {
            context.error("Invalid number of arguments");
            return;
        }
        String name = args.get(0).toLowerCase();
        Action action = EnumUtils.getIgnoreCase(Action.class, primaryArgument, Action.Display);
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

    private void display(String name, Context context) {
        Message message = manager.getMessage(name);
        if(message == null) {
            context.error("Message not found");
            return;
        }

        String text = message.getText();
        for(Map.Entry<String, String> entry : data.entrySet()) {
            text = text.replace('{' + entry.getKey() + '}', entry.getValue());
        }

        Source user = context.getUser();
        Server server = context.getServer();
        Channel channel = context.getChannel();

        text = text
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

        context.message(text);
    }

    private void get(String name, Context context) {
        if(!messages.containsKey(name)) {
            context.error("Message not found");
            return;
        }
        context.message(messages.get(name));
    }

    private void set(String name, String value, Context context) {
        messages.put(name, value);
        context.message(name + " message set");
    }

    private void delete(String name, Context context) {
        if(!messages.containsKey(name)) {
            context.error(name + " not found");
            return;
        }
        messages.remove(name);
        context.message(name + " deleted");
    }

    private enum Action {
        Display, Get, Set, Delete, Event
    }

}
