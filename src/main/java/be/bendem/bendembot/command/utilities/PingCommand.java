package be.bendem.bendembot.command.utilities;

import be.bendem.bendembot.command.BaseCommand;
import be.bendem.bendembot.Context;
import be.bendem.bendembot.utils.EnumUtils;

import java.util.List;

/**
 * @author bendem
 */
public class PingCommand extends BaseCommand {

    public PingCommand() {
        super("ping", new String[] {
            "Pings a user or a website - Usage ##.<user|site> [user|site]"
        });
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        Target target = EnumUtils.getIgnoreCase(Target.class, primaryArgument, Target.User);
        if(args.size() == 0 && target == Target.Site) {
            context.error("Missing arguments");
            return;
        }
        switch(target) {
            case User:
                pingUser(args.size() == 0 ? context.getUser().getName() : args.get(0));
                break;
            case Site:
                pingSite(args.get(0));
                break;
        }
    }

    private void pingUser(String nick) {
    }

    private void pingSite(String url) {
    }

    private enum Target {
        User, Site
    }

}
