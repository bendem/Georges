package be.bendem.bendembot.commands.bot;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.Georges;
import be.bendem.bendembot.commands.BaseCommand;

import java.io.IOException;
import java.util.List;

/**
 * @author bendem
 */
public class RestartCommand extends BaseCommand {

    public RestartCommand() {
        super("restart", new String[] { "Restarts me" }, true);
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        try {
            Runtime.getRuntime().exec("./georges restart");
        } catch(IOException e) {
            context.error("Command failed: " + e.getMessage());
            Georges.getLogger().error("Failed to launch restart command", e);
        }
    }
}
