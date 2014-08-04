package be.bendem.bendembot.command;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author bendem
 */
public class DataCommand extends BaseCommand {

    private final Map<String, String> data;
    private final Set<String> specials;

    public DataCommand(Map<String, String> data, Set<String> specials) {
        super("data", new String[]{"Set and get message data - Usage: data.<set|get> <key> [value]"}, true);
        this.data = data;
        this.specials = specials;
    }

    @Override
    protected void exec(String primaryArgument, List<String> args) {
        if(args.size() == 0) {
            error("Invalid number of arguments");
            return;
        }
        String key = args.get(0).toLowerCase();
        Action action = Action.get(primaryArgument);
        if(action == null) {
            error("Unknown action");
            return;
        }

        switch(action) {
            case Get:
                get(key);
                break;
            case Set:
                set(key, StringUtils.join(args.listIterator(1), ' '));
                break;
            case Delete:
                delete(key);
                break;
        }
    }

    private void get(String key) {
        if(!data.containsKey(key)) {
            error(key + " not found");
            return;
        }
        message(key + ": " + data.get(key));
    }

    private void set(String key, String value) {
        if(specials.contains(key)) {
            error("Can't override special values");
            return;
        }
        data.put(key, value);
        message(key + " value set");
    }

    private void delete(String key) {
        if(!data.containsKey(key)) {
            error(key + " not found");
            return;
        }
        data.remove(key);
        message(key + " deleted");
    }

    private enum Action {
        Get, Set, Delete;

        public static Action get(String name) {
            for(Action action : values()) {
                if(action.name().equalsIgnoreCase(name)) {
                    return action;
                }
            }
            return null;
        }
    }
}
