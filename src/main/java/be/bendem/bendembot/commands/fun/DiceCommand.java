package be.bendem.bendembot.commands.fun;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.commands.BaseCommand;
import fr.ribesg.alix.api.enums.Codes;

import java.util.List;
import java.util.Random;

/**
 * @author bendem
 */
public class DiceCommand extends BaseCommand {

    private final Random random;

    public DiceCommand() {
        super("dice", new String[] {
            "Roll some dices - Usage ## [count] [faces]"
        });
        random = new Random();
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        int count = 1;
        int faces = 6;
        if(args.size() > 0) {
            count = getInt(args.get(0), count);
        }
        if(args.size() > 1) {
            faces = getInt(args.get(1), faces);
        }
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < count; i++) {
            builder
                .append('<')
                .append(Codes.LIGHT_BLUE)
                .append(random.nextInt(faces) + 1)
                .append(Codes.RESET)
                .append("> ");
        }
        context.message(builder.toString());
    }
}
