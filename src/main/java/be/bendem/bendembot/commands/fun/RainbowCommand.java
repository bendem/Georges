package be.bendem.bendembot.commands.fun;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.commands.BaseCommand;
import fr.ribesg.alix.api.enums.Codes;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Random;

public class RainbowCommand extends BaseCommand {

    private static final String[] COLORS = new String[] {
        Codes.TEAL,
        Codes.LIGHT_CYAN,
        Codes.BLUE,
        Codes.LIGHT_BLUE,
        Codes.GREEN,
        Codes.LIGHT_GREEN,
        Codes.YELLOW,
        Codes.ORANGE,
        Codes.RED,
        Codes.BROWN,
        Codes.PURPLE,
        Codes.PINK,
    };

    private final Random RANDOM = new Random();

    public RainbowCommand() {
        super("rainbow", new String[] {
            "Rainbow unicorn - Usage ## <text>"
        }, "unicorn", "rb");
    }

    @Override
    protected void exec(Context context, String primaryArgument, List<String> args) {
        if(args.isEmpty()) {
            return;
        }

        StringBuilder buffer = new StringBuilder(StringUtils.join(args, ' '));
        int currentColor = RANDOM.nextInt(COLORS.length);
        for(int i = buffer.length() - 1; i >= 0; i--) {
            buffer.insert(i, COLORS[currentColor]);
            currentColor = (currentColor + 1) % COLORS.length;
        }
        context.message(buffer.toString());
    }
}
