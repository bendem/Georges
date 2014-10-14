package be.bendem.bendembot.commands.fun;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.commands.BaseCommand;

import java.util.List;

/**
 * @author bendem
 */
public class DanceCommand extends BaseCommand {

    public DanceCommand() {
        super("dance", new String[] { "Make me dance \\o/" });
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        if(context.getChannel() == null) {
            context.error("I'm not dancing alone in a PM D:");
            return;
        }
        context.message("<(^_^<)");
        context.message("(>^_^)>");
        context.message("\\(^_^)\\");
        context.message("/(^_^)/");
        context.message("\\(^_^)/");
    }
}
