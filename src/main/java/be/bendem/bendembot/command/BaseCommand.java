package be.bendem.bendembot.command;

import be.bendem.bendembot.utils.GistStacks;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Log;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.bot.command.Command;
import fr.ribesg.alix.api.enums.Codes;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author bendem
 */
/* package */ abstract class BaseCommand extends Command {

    protected Server server;
    protected Channel channel;
    protected Source user;

    public BaseCommand(String name) {
        this(name, null);
    }

    public BaseCommand(String name, String[] usage) {
        super(name, usage, false, null);
    }

    public BaseCommand(String name, String[] usage, String... aliases) {
        super(name, usage, false, null, aliases);
    }

    public BaseCommand(String name, String[] usage, boolean restricted) {
        super(name, usage, restricted, null);
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
        this.server = server;
        this.channel = channel;
        this.user = user;
        try {
            this.exec(primaryArgument, Arrays.asList(args));
        } catch(Exception e) {
            Log.error("Error while executing " + getClass().getSimpleName() + " command", e);
            error("A problem happened, you might want to check " + GistStacks.gist(e), false);
        } finally {
            this.server = null;
            this.channel = null;
            this.user = null;
        }
        return true;
    }

    protected abstract void exec(String primaryArgument, List<String> args);

    protected void error(String message) {
        error(message, true);
    }

    protected void error(String message, boolean includeName) {
        message(Codes.RED + (includeName ? user.getName() + ", " : "") + message, false);
    }

    protected void message(String message) {
        message(message, true);
    }

    protected void message(String message, boolean includeName) {
        (channel == null ? user : channel).sendMessage((includeName ? user.getName() + ", " : "") + message);
    }

}
