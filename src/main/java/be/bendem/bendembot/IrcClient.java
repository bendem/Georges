package be.bendem.bendembot;

import be.bendem.bendembot.command.bot.ChannelsCommand;
import be.bendem.bendembot.command.messages.DataCommand;
import be.bendem.bendembot.command.utilities.FarooCommand;
import be.bendem.bendembot.command.bot.JoinCommand;
import be.bendem.bendembot.command.bot.LeaveCommand;
import be.bendem.bendembot.command.messages.MessageCommand;
import be.bendem.bendembot.command.bot.NickCommand;
import be.bendem.bendembot.command.utilities.NickServCommand;
import be.bendem.bendembot.command.utilities.PingCommand;
import be.bendem.bendembot.command.bot.QuitCommand;
import be.bendem.bendembot.command.utilities.TwitterCommand;
import be.bendem.bendembot.custompackets.ActionIrcPacket;
import be.bendem.bendembot.filters.ChatFilter;
import be.bendem.bendembot.usermanagement.UserManager;
import be.bendem.bendembot.utils.Time;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Client;
import fr.ribesg.alix.api.Log;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.bot.command.CommandManager;
import fr.ribesg.alix.api.message.IrcPacket;
import fr.ribesg.alix.api.message.ModeIrcPacket;
import fr.ribesg.alix.api.message.PrivMsgIrcPacket;
import fr.ribesg.alix.api.network.ssl.SSLType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author bendem
 */
public class IrcClient extends Client {

    private final UserManager     userManager;
    private final Set<ChatFilter> filters;
    private final Set<String>     admins;
    private long lastSpoke = Time.now();
    private long lastJoke  = 0;

    public IrcClient(String name) {
        super(name);
        Log.debug("Starting up...");
        admins = new HashSet<>();
        admins.add("bendem");

        filters = new HashSet<>();
        userManager = new UserManager(this);
        loadItMyself();
        getServers().forEach(Server::connect);
    }

    @Override
    protected boolean load() {
        // Unused method, it clashes with final declarations because
        // it's called inside the super constructor
        return true;
    }

    public void loadItMyself() {
        Server server = new Server(this, "Esper", getName(), getName(), "irc.esper.net", 6697, null, SSLType.TRUSTING);
        server.addChannel("#Georges");
        //server.addChannel("#bendemPlugins");

        getServers().add(server);

        //AlixConfiguration configuration = new AlixConfiguration("alix.yml");

        CommandManager manager = createCommandManager("`", admins);
        manager.setUnknownCommandMessage(null);

        // Control commands
        manager.registerCommand(new JoinCommand());
        manager.registerCommand(new LeaveCommand());
        manager.registerCommand(new QuitCommand(this));
        manager.registerCommand(new NickCommand(this));
        manager.registerCommand(new ChannelsCommand());

        // Utility commands
        manager.registerCommand(new TwitterCommand());
        manager.registerCommand(new FarooCommand());
        manager.registerCommand(new NickServCommand(this));
        manager.registerCommand(new PingCommand());

        // Message commands
        Map<String, String> data = new HashMap<>();
        Set<String> specials = new HashSet<>();
        manager.registerCommand(new DataCommand(data, specials));
        manager.registerCommand(new MessageCommand(data, specials));

        // JavaDoc commands
        //manager.registerCommand(new BukkitCommand(this));
        //manager.registerCommand(new JavaCommand(this));


        //filters.add(new SpamFilter(this));
        //filters.add(new MessageFilter(this));
    }

    @Override
    public void onServerJoined(final Server server) {
        auth(server);
    }

    public void auth(final Server server) {
        if(server.getName().equals("Esper")) {
            server.send(new PrivMsgIrcPacket("NickServ", "IDENTIFY espc0waychal@"));
        }
    }

    @Override
    public void onClientJoinChannel(final Channel channel) {
        //channel.sendMessage("\\o");
    }

    @Override
    public void onRawIrcMessage(Server server, IrcPacket ircPacket) {
        if(ircPacket instanceof ModeIrcPacket) {
            Log.info("MODE Packet handled");
            ModeIrcPacket modePacket = (ModeIrcPacket) ircPacket;
            String[] parameters = modePacket.getParameters();
            if(parameters.length < 1) {
                // Unhandled MODE packet
                return;
            }

            Channel channel = server.getChannel(parameters[0]);
            if(channel != null) {
                Log.debug("Updating users in " + channel.getName());
                channel.updateUsers();
            }
        }
    }

    @Override
    public void onChannelMessage(final Channel channel, final Source author, final String message) {
        for(ChatFilter filter : filters) {
            if(filter.handleMessage(channel, author, message)) {
                break;
            }
        }

        if(Time.since(lastJoke) > 30_000) {
            // Curlybear is op
            if(message.toLowerCase().contains("hue hue hue") && !isBotAdmin(author.getName())) {
                channel.getServer().send(new ActionIrcPacket(channel.getName(), "instanciates his ban utility..."));
                lastJoke = Time.now();
            } else if(message.toLowerCase().startsWith("hue") && author.getName().toLowerCase().contains("bear")) {
                channel.sendMessage("hue? I guess you mean HUE HUE HUE!?");
                lastJoke = Time.now();
            }
        }

    }

    @Override
    public void onUserPartChannel(Source source, Channel channel) {
        for(ChatFilter filter : filters) {
            filter.forgetUser(source, channel);
        }
    }

    public boolean speak(Channel channel, String...messages) {
        return speak(false, channel, messages);
    }

    public boolean speak(boolean force, Channel channel, String...messages) {
        if(messages == null || messages.length < 1) {
            return false;
        }
        if(!force && Time.since(getLastSpoke()) < 10_000) {
            return false;
        }
        channel.sendMessage(messages);
        resetLastSpoke();
        return true;
    }

    public long getLastSpoke() {
        return lastSpoke;
    }

    public void setLastSpoke(long lastWarning) {
        this.lastSpoke = lastWarning;
    }

    public void resetLastSpoke() {
        lastSpoke = Time.now();
    }

    public boolean isBotAdmin(String name) {
        return admins.contains(name);
    }

    public UserManager getUserManager() {
        return userManager;
    }

    // Let's kill that bot correctly
    private volatile boolean killed = false;

    @Override
    public void kill() {
        if(killed) {
            return;
        }
        killed = true;
        try {
            super.kill();
            Log.info("Exited!");
        } catch(Throwable t) {
            Log.error("Could not kill bot", t);
            killed = false;
        }
        System.exit(0);
    }

}
