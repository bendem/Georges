package be.bendem.bendembot.command.messages;

import be.bendem.bendembot.automatedmessages.MessageData;
import be.bendem.bendembot.automatedmessages.MessageManager;
import be.bendem.bendembot.command.BaseCommand;
import be.bendem.bendembot.Context;
import be.bendem.bendembot.utils.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author bendem
 */
public class DataCommand extends BaseCommand {

    private final MessageManager manager;

    public DataCommand(MessageManager manager) {
        super("data", new String[]{
            "Set and get message data - Usage: data.<set|get> <key> [value]"},
        true);
        this.manager = manager;
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        if(args.size() == 0) {
            context.error("Invalid number of arguments");
            return;
        }
        String key = args.get(0).toLowerCase();
        Action action = EnumUtils.getIgnoreCase(Action.class, primaryArgument);
        if(action == null) {
            context.error("Unknown action");
            return;
        }

        switch(action) {
            case Get:
                get(key, context);
                break;
            case Set:
                set(key, StringUtils.join(args.listIterator(1), ' '), context);
                break;
            case Delete:
                delete(key, context);
                break;
        }
    }

    private void get(String key, Context context) {
        if(manager.getData(key) == null) {
            context.error(key + " not found");
            return;
        }
        context.message(key + ": " + manager.getData(key));
    }

    private void set(String key, String value, Context context) {
        if(EnumUtils.getIgnoreCase(MessageData.class, key) != null) {
            context.error("Can't override special values");
            return;
        }
        manager.setData(key, value);
        context.message(key + " value set");
    }

    private void delete(String key, Context context) {
        if(manager.getData(key) == null) {
            context.error(key + " not found");
            return;
        }
        manager.removeData(key);
        context.message(key + " deleted");
    }

    private enum Action {
        Get, Set, Delete
    }

}
