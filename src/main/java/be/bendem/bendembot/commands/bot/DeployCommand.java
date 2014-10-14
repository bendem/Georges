package be.bendem.bendembot.commands.bot;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.Georges;
import be.bendem.bendembot.commands.BaseCommand;

import java.io.IOException;
import java.util.List;

/**
 * @author bendem
 */
public class DeployCommand extends BaseCommand {

    public DeployCommand() {
        super("deploy", new String[] { "Tries to deploy myself" }, true);
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        try {
            Runtime.getRuntime().exec("./georges deploy");
        } catch(IOException e) {
            context.error("Command failed: " + e.getMessage());
            Georges.getLogger().error("Failed to launch deploy command", e);
        }
    }
}
