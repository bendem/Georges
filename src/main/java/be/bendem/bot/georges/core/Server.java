package be.bendem.bot.georges.core;

import com.ircclouds.irc.api.IServerParameters;
import com.ircclouds.irc.api.domain.IRCServer;

import java.util.List;

/**
 * @author bendem
 */
public class Server implements IServerParameters {

    private final String nickname;
    private final List<String> alts;
    private final String ident;
    private final String realname;
    private final IRCServer server;

    public Server(String nickname, List<String> alts, String ident, String realname, IRCServer server) {
        this.nickname = nickname;
        this.alts = alts;
        this.ident = ident;
        this.realname = realname;
        this.server = server;
    }

    /**
     * Returns the desired to use nickname
     */
    @Override
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns desired to use alternative nicknames
     */
    @Override
    public List<String> getAlternativeNicknames() {
        return alts;
    }

    /**
     * Returns the desired to use ident
     */
    @Override
    public String getIdent() {
        return ident;
    }

    /**
     * Returns the desired to use real name
     */
    @Override
    public String getRealname() {
        return realname;
    }

    /**
     * Returns the desired to use {@link com.ircclouds.irc.api.domain.IRCServer}
     */
    @Override
    public IRCServer getServer() {
        return server;
    }
}
