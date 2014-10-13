package be.bendem.bendembot.commands.fun;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.commands.BaseCommand;
import fr.ribesg.alix.api.enums.Codes;

import java.util.List;
import java.util.Random;

/**
 * @author bendem
 */
public class FlipCommand extends BaseCommand {

    private static final Random RANDOM = new Random();

    public FlipCommand() {
        super("flip", new String[] {
            "Flips a coin - Usage: ##"
        }, "coin");
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        context.message("<" + Codes.LIGHT_BLUE + (RANDOM.nextBoolean() ? "head" : "tail") + Codes.RESET + ">");
    }
}
