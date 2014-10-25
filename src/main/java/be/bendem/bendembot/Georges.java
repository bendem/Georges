package be.bendem.bendembot;

import be.bendem.bendembot.automatedmessages.MessageManager;
import be.bendem.bendembot.chathandling.ChatManager;
import be.bendem.bendembot.chathandling.QuestionChat;
import be.bendem.bendembot.chathandling.TwitterChat;
import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.commands.bot.*;
import be.bendem.bendembot.commands.fun.*;
import be.bendem.bendembot.commands.messages.DataCommand;
import be.bendem.bendembot.commands.messages.MessageCommand;
import be.bendem.bendembot.commands.utilities.*;
import be.bendem.bendembot.configuration.Configuration;
import be.bendem.bendembot.usermanagement.UserManager;
import be.bendem.bendembot.utils.Time;
import be.bendem.bendembot.utils.TwitterApiUtils;
import fr.ribesg.alix.api.Client;
import fr.ribesg.alix.api.EventManager;
import fr.ribesg.alix.api.Log;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.bot.util.PasteUtil;
import fr.ribesg.alix.api.event.ChannelMessageEvent;
import fr.ribesg.alix.api.event.EventHandler;
import fr.ribesg.alix.api.message.PrivMsgIrcPacket;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Set;

/**
 * @author bendem
 */
public class Georges extends Client {

    private final Configuration  configuration;
    private final UserManager    userManager;
    private final MessageManager messageManager;
    private long lastSpoke = Time.now();
    private long lastJoke  = 0;

    public Georges(String name) {
        super(name);
        logger.info("Starting up...");

        PasteUtil.setMode(null);

        configuration = new Configuration(this);
        if(!configuration.exists()) {
            try {
                configuration.save();
            } catch(IOException e) {
                Georges.getLogger().error("Failed to save config", e);
            }
        }

        userManager = new UserManager();
        messageManager = new MessageManager(userManager);
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
        getServers().addAll(configuration.getServers());

        EventManager.register(new BotHandler(this));

        TwitterApiUtils twitterApiUtils = new TwitterApiUtils(configuration);

        ChatManager chatManager = new ChatManager();
        chatManager.register(new TwitterChat(twitterApiUtils));
        chatManager.register(new QuestionChat());
        //chatManager.register(new ViReplaceChat()); // TODO Finish that when Georges can remember previous message of someone

        createCommandManager("`", configuration.getAdmins())
            .setUnknownCommandMessage(null);

        // Control commands
        register(new ChannelCommand(this));
        register(new DeployCommand());
        register(new NickCommand());
        register(new QuitCommand(this));
        register(new RestartCommand());
        register(new TellCommand(this));

        // Utility commands
        register(new FarooCommand(configuration.getFarooKey()));
        register(new NashornCommand());
        register(new NickServCommand(this));
        register(new PingCommand());
        register(new StatCommand());
        register(new TwitterCommand(twitterApiUtils));
        register(new UserCommand(this));

        // Fun commands
        register(new ChooseCommand());
        register(new DanceCommand());
        register(new DiceCommand());
        register(new FlipCommand());
        register(new QuoteCommand());

        // Message commands
        register(new DataCommand(messageManager));
        register(new MessageCommand(messageManager));

        EventManager.register(new Object() {
            @EventHandler
            public void onMessage(ChannelMessageEvent e) {
                if(!e.getMessage().startsWith("!")) {
                    return;
                }
                String[] parts = e.getMessage().substring(1).split("\\w+");
                if(parts.length == 0) {
                    return;
                }
                Context context = new Context(e.getChannel(), e.getUser());
                String message = messageManager.getTransformedMessage(parts[0], context);
                if(message != null) {
                    context.message(message);
                }
            }
        });
    }

    private void register(BaseCommand command) {
        getCommandManager().registerCommand(command);
    }

    public void auth(final Server server) {
        if(server.getName().equals("Esper")) {
            server.send(new PrivMsgIrcPacket("NickServ", "IDENTIFY " + configuration.getEsperPass()));
        }
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
        return configuration.getAdmins().contains(name);
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public Set<String> getAdmins() {
        return configuration.getAdmins();
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
            logger.info("Exited!");
        } catch(Throwable t) {
            logger.error("Could not kill bot", t);
            killed = false;
        }
        System.exit(0);
    }

    public long getLastJoke() {
        return lastJoke;
    }

    public void setLastJoke(long lastJoke) {
        this.lastJoke = lastJoke;
    }

    public static void main(final String args[]) {
        Log.get().setLevel(Level.INFO);
        Georges georges = new Georges("Georges");

        while(!System.console().readLine().equalsIgnoreCase("stop"));
        georges.kill();
    }

    private static final Logger logger = Logger.getLogger("Georges");
    static {
        logger.setLevel(Level.ALL);
    }

    public static Logger getLogger() {
        return logger;
    }

}
