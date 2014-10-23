package be.bendem.bot.georges.core.command;

import com.ircclouds.irc.api.domain.IRCChannel;
import com.ircclouds.irc.api.domain.IRCServer;
import com.ircclouds.irc.api.domain.IRCUser;

import java.util.List;

/**
 * @author bendem
 */
public interface Command {

    public String getName();
    public void onCommand(IRCUser user, IRCChannel channel, IRCServer server, List<String> args);

}
