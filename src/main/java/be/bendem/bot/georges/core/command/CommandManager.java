package be.bendem.bot.georges.core.command;

import be.bendem.bot.georges.Georges;
import com.ircclouds.irc.api.IRCApi;
import com.ircclouds.irc.api.domain.IRCUser;
import com.ircclouds.irc.api.domain.messages.ChannelPrivMsg;
import com.ircclouds.irc.api.listeners.VariousMessageListenerAdapter;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bendem
 */
public class CommandManager {

    private final Georges bot;
    private final String prefix;
    private final Map<String, Command> commands;

    public CommandManager(Georges bot, String prefix) {
        this.bot = bot;
        this.prefix = prefix;
        this.commands = new HashMap<>();

        bot.getApi().addListener(new VariousMessageListenerAdapter() {
            @Override
            public void onChannelMessage(ChannelPrivMsg event) {
                String msg = event.getText();
                if(msg.startsWith(prefix)) {
                    handleCommand(msg.substring(prefix.length()), event.getSource(), event.getChannelName());
                }
            }
        });
    }

    private void handleCommand(String msg, IRCUser source, String channelName) {
        String[] args = msg.split("\\w+");
        Command command;
        if(args.length == 0 || (command = commands.get(args[0])) == null) {
            return;
        }
        command.onCommand(
                source,
                bot.getState().getChannelByName(channelName),
                bot.getState().getServer(),
                Arrays.asList(args).subList(1, args.length));
    }

    public void register(Command command) {
        Validate.isTrue(!commands.containsKey(command.getName()), "This command name is already registered");
        commands.put(command.getName(), command);
    }

}
