package be.bendem.bendembot.commands.bot;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.Georges;
import be.bendem.bendembot.commands.BaseCommand;

import java.util.List;

/**
 * @author bendem
 */
public class QuitCommand extends BaseCommand {

    private final Georges bot;

    public QuitCommand(Georges bot) {
        super("quit", new String[]{
            "Kill the bot - Usage ##"
        }, true, "q");
        this.bot = bot;
    }

    @Override
    public void exec(Context context, String primaryArgument, List<String> args) {
        context.getServer().disconnect("See ya on the road to hell!");
        bot.kill();
    }

}
