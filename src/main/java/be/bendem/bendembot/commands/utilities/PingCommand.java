package be.bendem.bendembot.commands.utilities;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.utils.StrUtils;

import java.util.List;

/**
 * @author bendem
 */
public class PingCommand extends BaseCommand {

    public PingCommand() {
        super("ping", new String[] {
            "Pings a user or a website - Usage: ## [user|site]"
        }, "pi");
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        if(args.size() == 0) {
            context.message("Pong.", false);
            return;
        }
        Target target = StrUtils.isUrl(args.get(0)) ? Target.Site : Target.User;
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
