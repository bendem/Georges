package be.bendem.bendembot.command;

import be.bendem.bendembot.IrcClient;

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
    public void exec(String primaryArgument, List<String> args) {
        server.disconnect("See ya on the road to hell!");
        bot.kill();
    }

}
