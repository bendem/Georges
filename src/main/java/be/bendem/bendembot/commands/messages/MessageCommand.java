package be.bendem.bendembot.commands.messages;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.automatedmessages.Message;
import be.bendem.bendembot.automatedmessages.MessageEventHandler;
import be.bendem.bendembot.automatedmessages.MessageManager;
import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.utils.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author bendem
 */
public class MessageCommand extends BaseCommand {

    private static final String NOT_FOUND_MESSAGE = "'%s' message not found";
    private final MessageManager manager;

    public MessageCommand(MessageManager manager) {
        super("message", new String[] {
            "Message manipulation - Usage: ##.<" + EnumUtils.joinValues(Action.class, "|") + "> <name> [value(s)]",
            "Events availables are " + EnumUtils.joinValues(MessageEventHandler.Event.class, ", ")
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
            case Channel:
                channel(name, args.subList(1, args.size()), context);
                break;
            case Event:
            case RemoveEvent:
                event(name, context, args.subList(1, args.size()), action == Action.Event);
                break;
            case ClearEvents:
                clearEvents(name, context);
                break;
            case Delete:
                delete(name, context);
                break;
        }
    }

    private void display(String name, Context context) {
        String message = manager.getTransformedMessage(name, context);
        if(message == null) {
            context.error(String.format(NOT_FOUND_MESSAGE, name));
            return;
        }
        context.message(message);
    }

    private void get(String name, Context context) {
        Message message = manager.getMessage(name);
        if(message == null) {
            context.error(String.format(NOT_FOUND_MESSAGE, name));
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

    private void channel(String name, List<String> args, Context context) {
        Message message = manager.getMessage(name);
        if(message == null) {
            context.error(String.format(NOT_FOUND_MESSAGE, name));
            return;
        }
        if(args.size() < 1) {
            message.setChannel(null);
            context.message("Channel set to global");
        } else {
            String channel = args.get(0).startsWith("#") ? args.get(0) : '#' + args.get(0);
            message.setChannel(channel);
            context.message("Channel set to " + channel);
        }
    }

    private void event(String name, Context context, List<String> events, boolean add) {
        Message message = manager.getMessage(name);
        if(message == null) {
            context.error(String.format(NOT_FOUND_MESSAGE, name));
            return;
        }
        List<String> errored = new LinkedList<>();
        for(String eventName : events) {
            MessageEventHandler.Event event = EnumUtils.getIgnoreCase(MessageEventHandler.Event.class, eventName);
            if(event == null) {
                errored.add(eventName);
            } else {
                if(add) {
                    message.addEvent(event);
                } else {
                    message.removeEvent(event);
                }
            }
        }
        if(errored.size() == 0) {
            context.message(events.size() + " events added.");
        } else {
            context.error(StringUtils.join(errored, ", ") + ' ' + (errored.size() > 1 ? "were" : "was") +  " not found.");
        }
    }

    private void clearEvents(String name, Context context) {
        Message message = manager.getMessage(name);
        if(message == null) {
            context.error(String.format(NOT_FOUND_MESSAGE, name));
            return;
        }
        context.message(message.clearEvents() + " events cleared.");
    }

    private void delete(String name, Context context) {
        Message message = manager.getMessage(name);
        if(message == null) {
            context.error(String.format(NOT_FOUND_MESSAGE, name));
            return;
        }
        manager.removeMessage(name);
        context.message(name + " message deleted");
    }

    private enum Action {
        Display, Get, Set, Event, Channel, RemoveEvent, ClearEvents, Delete
    }

}
