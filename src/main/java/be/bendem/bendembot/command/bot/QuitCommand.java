package be.bendem.bendembot.command.bot;

import be.bendem.bendembot.IrcClient;
import be.bendem.bendembot.command.BaseCommand;
import be.bendem.bendembot.Context;

import java.util.List;

/**
 * @author bendem
 */
public class QuitCommand extends BaseCommand {

    private final IrcClient bot;

    public QuitCommand(IrcClient bot) {
        super("quit", new String[]{ "Kill the bot - Usage ##" }, true, "q");
        this.bot = bot;
    }

    @Override
    public void exec(Context context, String primaryArgument, List<String> args) {
        context.getServer().disconnect("See ya on the road to hell!");
        bot.kill();
    }

}
