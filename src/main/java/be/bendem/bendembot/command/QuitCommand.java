package be.bendem.bendembot.command;

import be.bendem.bendembot.IrcClient;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.bot.command.Command;

/**
 * @author bendem
 */
public class QuitCommand extends Command {

    private final IrcClient bot;

    public QuitCommand(IrcClient bot) {
        super(bot.getCommandManager(), "quit", new String[]{ "Quit" }, true, null, "q");
        this.bot = bot;
    }

    @Override
    public void exec(Server server, Channel channel, Source source, String s, String[] strings) {
        server.disconnect("See ya on the road to hell!");
        bot.kill();
    }

}
