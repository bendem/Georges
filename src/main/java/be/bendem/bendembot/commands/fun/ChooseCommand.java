package be.bendem.bendembot.commands.fun;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.commands.BaseCommand;

import java.util.List;
import java.util.Random;

/**
 * @author bendem
 */
public class ChooseCommand extends BaseCommand {

    private final Random RANDOM = new Random();

    public ChooseCommand() {
        super("choose", new String[] {
            "Chooses between propositions - Usage: ## <choice 1>[, next choices...]"
        });
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        if(args.size() == 0) {
            context.error("no choices :(");
            return;
        }
        String[] choices = String.join(" ", args).split("\\s*,\\s*");
        if(choices.length == 1) {
            context.message("Is there really a choice to make? " + choices[0]);
        } else {
            context.message(choices[RANDOM.nextInt(choices.length)]);
        }
    }
}
