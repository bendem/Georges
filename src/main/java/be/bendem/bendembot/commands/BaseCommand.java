package be.bendem.bendembot.commands;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.utils.GistStacks;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Log;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.bot.command.Command;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author bendem
 */
public abstract class BaseCommand extends Command {

    public BaseCommand(String name) {
        this(name, null);
    }

    public BaseCommand(String name, String[] usage) {
        this(name, usage, false);
    }

    public BaseCommand(String name, String[] usage, String... aliases) {
        this(name, usage, false, null, aliases);
    }

    public BaseCommand(String name, String[] usage, boolean restricted) {
        this(name, usage, restricted, (Set<String>) null);
    }

    public BaseCommand(String name, String[] usage, boolean restricted, String... aliases) {
        this(name, usage, restricted, null, aliases);
    }

    public BaseCommand(String name, String[] usage, boolean restricted, Set<String> allowedNickNames, String... aliases) {
        super(name, usage, restricted, allowedNickNames, aliases);
    }

    /**
     * Executes this Command.
     *
     * @param server the Server this Command has been called from
     * @param channel the Channel this Command has been called in, or
     * null if there's none (i.e. if it's a private message)
     * @param user the User that wrote the Command
     * @param primaryArgument argument passed as commandPrefix.primaryArgument
     * @param args arguments passed the the Command
     *
     * @return false if the CommandManager should print the usage, true otherwise
     */
    @Override
    public synchronized boolean exec(Server server, Channel channel, Source user, String primaryArgument, String[] args) {
        Context context = new Context(server, channel, user);
        try {
            this.exec(context, primaryArgument, Arrays.asList(args));
        } catch(Exception e) {
            Log.error("Error while executing " + getClass().getSimpleName() + " command", e);
            context.error("A problem happened, you might want to check " + GistStacks.gist(e), false);
        }
        return true;
    }

    protected abstract void exec(Context context, String primaryArgument, List<String> args);

    protected static int getInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return def;
        }
    }

}
