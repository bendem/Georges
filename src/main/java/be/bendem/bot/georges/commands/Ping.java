package be.bendem.bot.georges.commands;

import be.bendem.bot.georges.core.command.Command;
import com.ircclouds.irc.api.IRCApi;
import com.ircclouds.irc.api.domain.IRCChannel;
import com.ircclouds.irc.api.domain.IRCUser;

import java.util.List;

/**
 * @author bendem
 */
public class Ping implements Command {

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public void onCommand(IRCUser user, IRCChannel channel, IRCApi api, List<String> args) {
        api.message(channel.getName(), "Pong.");
    }

}
