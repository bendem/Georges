package be.bendem.bendembot;

import be.bendem.bendembot.command.BukkitCommand;
//import be.bendem.bendembot.command.DebugCommand;
import be.bendem.bendembot.command.JavaCommand;
import be.bendem.bendembot.command.QuitCommand;
import be.bendem.bendembot.custompackets.ActionIrcPacket;
import be.bendem.bendembot.filters.ChatFilter;
import be.bendem.bendembot.filters.MessageFilter;
import be.bendem.bendembot.filters.SpamFilter;
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

import java.util.HashSet;
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
        for(Server server : getServers()) {
            server.connect();
        }
    }

    @Override
    protected void load() {}

    public void loadItMyself() {
        Server server = new Server(this, getName(), "irc.esper.net", 6697, SSLType.TRUSTING);
        server.addChannel("#alixNbendem");
        //server.addChannel("#bendemPlugins");

        getServers().add(server);

        createCommandManager(")", admins);
        CommandManager manager = getCommandManager();
        manager.setUnknownCommandMessage(null);

        manager.registerCommand(new BukkitCommand(this));
        manager.registerCommand(new JavaCommand(this));
        //manager.registerCommand(new DebugCommand(this));
        manager.registerCommand(new QuitCommand(this));
        //
        //
        //filters.add(new SpamFilter(this));
        //filters.add(new MessageFilter(this));
    }

    @Override
    public void kill() {
        // Stop threads here
        super.kill();
        Log.info("Exited!");
    }

    @Override
    public void onServerJoined(final Server server) {
        server.send(new PrivMsgIrcPacket("NickServ", "IDENTIFY espc0waychal@"));
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
                try {
                    channel.updateUsers(false);
                } catch(InterruptedException e) {
                    Log.error("Update error", e);
                }
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

    @Override
    public void onPrivateMessage(Server server, Source from, String message) {
        //String[] args = message.split(" ");
        //if(args.length < 1) {
        //    throw new IllegalArgumentException();
        //}
        //
        //String command = args[0];
        //List<String> argList = new ArrayList<>();
        //Collections.addAll(argList, args);
        //argList.remove(0);
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

}
