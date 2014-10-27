package be.bendem.bot.georges.core.command;

import com.ircclouds.irc.api.IRCApi;
import com.ircclouds.irc.api.domain.IRCChannel;
import com.ircclouds.irc.api.domain.IRCServer;
import com.ircclouds.irc.api.domain.IRCUser;
import com.ircclouds.irc.api.state.IIRCState;

import java.util.List;

/**
 * @author bendem
 */
public interface Command {

    public String getName();
    public void onCommand(IRCUser user, IRCChannel channel, IRCApi api, List<String> args);

}
