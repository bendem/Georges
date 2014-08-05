package be.bendem.bendembot.command.utilities;

import be.bendem.bendembot.command.BaseCommand;
import be.bendem.bendembot.command.CommandContext;

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
    protected void exec(CommandContext context, String primaryArgument, List<String> args) {
        if(primaryArgument == null) {
            primaryArgument = "user";
        }
        if(args.size() == 0 && "site".equals(primaryArgument)) {
            context.error("Missing arguments");
            return;
        }
        if("user".equals(primaryArgument)) {
            pingUser(args.size() == 0 ? context.getUser().getName() : args.get(0));
        } else if("site".equals(primaryArgument)) {
            pingSite(args.get(0));
        }
    }

    private void pingUser(String nick) {

    }

    private void pingSite(String url) {

    }

}
