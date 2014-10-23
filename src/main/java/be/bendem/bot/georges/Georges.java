package be.bendem.bot.georges;

import be.bendem.bot.georges.core.CallBack;
import be.bendem.bot.georges.core.Server;
import be.bendem.bot.georges.core.command.CommandManager;
import com.ircclouds.irc.api.IRCApi;
import com.ircclouds.irc.api.IRCApiImpl;
import com.ircclouds.irc.api.domain.IRCServer;
import com.ircclouds.irc.api.state.IIRCState;

import java.util.Arrays;

/**
 * @author bendem
 */
public class Georges {

    private final IRCApi api;
    private final CommandManager commandManager;
    private IIRCState state;

    public Georges() {
        api = new IRCApiImpl(true);
        commandManager = new CommandManager(this, "`");
    }

    public void start() {
        api.connect(
                new Server("Georges", Arrays.asList("Georges_"), "ident", "realname", new IRCServer("availo.esper.net", 6697, true)),
                new CallBack<>(
                        state -> this.state = state,
                        exception -> {
                            exception.printStackTrace();
                            System.exit(1); // Should be kill()
                        }));
    }

    public IRCApi getApi() {
        return api;
    }

    public IIRCState getState() {
        return state;
    }

    public static void main(String[] args) {
        new Georges().start();
    }

}
