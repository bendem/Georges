package be.bendem.bendembot.command;

import be.bendem.bendembot.BendemBot;
import be.bendem.bendembot.IrcClient;

/**
 * @author bendem
 */
public class BukkitCommand extends AbstractJdCommand {

    private final IrcClient bot;

    public BukkitCommand(IrcClient bot) {
        super("bukkit", new String[]{"Search the bukkit jds"}, "b");
        this.bot = bot;
    }

    @Override
    public String getUrl() {
        return BendemBot.BUKKIT_URL;
    }
}
