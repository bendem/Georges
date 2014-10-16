package be.bendem.bendembot.commands.fun;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.utils.Time;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author bendem
 */
public class DanceCommand extends BaseCommand {

    private long lastUsed = 0;

    public DanceCommand() {
        super("dance", new String[] { "Make me dance \\o/" });
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        if(context.getChannel() == null) {
            context.error("I'm not dancing alone in a PM D:");
            return;
        }
        if(Time.since(lastUsed) < TimeUnit.MINUTES.toMillis(1)) {
            context.action("doesn't want to dance anymore");
            return;
        }
        lastUsed = Time.now();
        context.message("<(^_^<)", false);
        context.message("(>^_^)>", false);
        context.message("\\(^_^)\\", false);
        context.message("/(^_^)/", false);
        context.message("\\(^_^)/", false);
    }
}
