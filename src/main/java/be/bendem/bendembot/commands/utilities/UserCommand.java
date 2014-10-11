package be.bendem.bendembot.commands.utilities;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.Georges;
import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.utils.EnumUtils;
import be.bendem.bendembot.utils.ModeUtils;

import java.util.List;
import java.util.Set;

/**
 * @author bendem
 */
public class UserCommand extends BaseCommand {

    private final Set<String> admins;

    public UserCommand(Georges georges) {
        super("user", new String[]{
            "User management command - Usage: ##.<" + EnumUtils.joinValues(Action.class, "|") + "> <target>"
        }, "u");
        admins = georges.getAdmins();
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        Action action = EnumUtils.getIgnoreCase(Action.class, primaryArgument, Action.Seen);
        if(action.isRestricted() && !admins.contains(context.getUser().getName())) {
            context.error("You don't have the right to use that command :/");
            return;
        }

        if(args.size() == 0) {
            context.error("Not enough argument");
            return;
        }

        String target = args.get(0);

        // TODO Add global to do the action in every chan the bot is opped in
        //boolean global = false;
        //if(args.size() > 1 && args.get(1).equalsIgnoreCase("true")) {
        //    global = true;
        //}

        switch(action) {
            case Ban:
                ModeUtils.ban(context.getServer(), context.getChannel(), target);
                break;
            case Op:
                ModeUtils.op(context.getServer(), context.getChannel(), target);
                break;
            case Kick:
                break;
            case Seen:
                break;
            case Voice:
                break;
        }
    }

    private enum Action {
        Ban, Op, Kick, Seen(false), Voice;

        private final boolean restricted;

        private Action() {
            this(true);
        }

        private Action(boolean restricted) {
            this.restricted = restricted;
        }

        public boolean isRestricted() {
            return restricted;
        }

        public static Action get(String name) {
            for(Action action : values()) {
                if(action.name().equalsIgnoreCase(name)) {
                    return action;
                }
            }
            return Seen;
        }
    }

}
