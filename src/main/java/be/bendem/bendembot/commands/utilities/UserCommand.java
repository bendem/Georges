package be.bendem.bendembot.commands.utilities;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.Georges;
import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.custompackets.KickIrcPacket;
import be.bendem.bendembot.utils.EnumUtils;
import be.bendem.bendembot.utils.ModeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author bendem
 */
public class UserCommand extends BaseCommand {

    private final Georges bot;

    public UserCommand(Georges bot) {
        super("user", new String[]{
            "User management command - Usage: ##.<" + EnumUtils.joinValues(Action.class, "|") + "> <target>"
        }, "u");
        this.bot = bot;
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        Action action = EnumUtils.getIgnoreCase(Action.class, primaryArgument, Action.Seen);
        if(action.isRestricted() && !bot.getAdmins().contains(context.getUser().getName())) {
            context.error("You don't have the right to use that command :/");
            return;
        }

        if(!context.getChannel().isOp(bot.getName())) {
            context.error("I'm not op in this channel");
            return;
        }

        if(args.size() == 0) {
            context.error("Not enough argument");
            return;
        }

        String target = args.get(0);

        switch(action) {
            case Ban:
                ModeUtils.ban(context.getServer(), context.getChannel(), target);
                // Fall through, ban causes kick
            case Kick:
                String reason = "You know why";
                if(args.size() > 1) {
                    reason = StringUtils.join(args.listIterator(1), ' ');
                }
                context.getServer().send(new KickIrcPacket(context.getChannel(), target, reason));
                break;
            case Op:
                ModeUtils.op(context.getServer(), context.getChannel(), target);
                break;
            case Voice:
                ModeUtils.voice(context.getServer(), context.getChannel(), target);
                break;
            case Seen:
                // TODO
                context.error("Not implemented yet");
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
