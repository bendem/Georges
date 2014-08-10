package be.bendem.bendembot.commands.messages;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.automatedmessages.Message;
import be.bendem.bendembot.automatedmessages.MessageManager;
import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.utils.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
        String message = manager.getTransformedMessage(name.toLowerCase(), context);
        if(message == null) {
            context.error("Message not found");
            return;
        }

        context.message(message);
    }

    private void get(String name, Context context) {
        Message message = manager.getMessage(name);
        if(message == null) {
            context.error("Message not found");
            return;
        }
        context.message(message.getText());
    }

    private void set(String name, String value, Context context) {
        Message message = manager.getMessage(name);
        if(message == null) {
            manager.setMessage(new Message(name, value));
        } else {
            message.setText(value);
        }
        context.message(name + " message set");
    }

    private void delete(String name, Context context) {
        Message message = manager.getMessage(name);
        if(message == null) {
            context.error(name + " not found");
            return;
        }
        manager.removeMessage(name);
        context.message(name + " deleted");
    }

    private enum Action {
        Display, Get, Set, Delete, Event
    }

}
