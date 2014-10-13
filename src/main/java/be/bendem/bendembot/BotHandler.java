package be.bendem.bendembot;

import be.bendem.bendembot.custompackets.ActionIrcPacket;
import be.bendem.bendembot.utils.Time;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.Source;
import fr.ribesg.alix.api.event.ChannelMessageEvent;
import fr.ribesg.alix.api.event.ClientJoinChannelEvent;
import fr.ribesg.alix.api.event.ClientKickedFromChannelEvent;
import fr.ribesg.alix.api.event.ClientLostConnectionEvent;
import fr.ribesg.alix.api.event.EventHandler;
import fr.ribesg.alix.api.event.FailedToJoinServerEvent;
import fr.ribesg.alix.api.event.ReceivedPacketEvent;
import fr.ribesg.alix.api.event.ServerJoinEvent;
import fr.ribesg.alix.api.event.UserPartChannelEvent;
import fr.ribesg.alix.api.message.IrcPacket;
import fr.ribesg.alix.api.message.ModeIrcPacket;

public class BotHandler {

    private final Georges bot;

    public BotHandler(Georges bot) {
        this.bot = bot;
    }

    @EventHandler
    public void onServerJoined(final ServerJoinEvent e) {
        bot.auth(e.getServer());
    }

    @EventHandler
    public void onClientJoinChannel(final ClientJoinChannelEvent e) {
        //e.getChannel().sendMessage("\\o");
    }

    @EventHandler
    public void onRawIrcMessage(ReceivedPacketEvent e) {
        // Workaournd because Alix does not update user modes
        Server server = e.getSource();
        IrcPacket ircPacket = e.getPacket();

        if(ircPacket instanceof ModeIrcPacket) {
            Georges.getLogger().info("MODE Packet handled");
            ModeIrcPacket modePacket = (ModeIrcPacket) ircPacket;
            String[] parameters = modePacket.getParameters();
            if(parameters.length < 1) {
                // Unhandled MODE packet
                return;
            }

            Channel channel = server.getChannel(parameters[0]);
            if(channel != null) {
                channel.updateUsers();
            }
        }
    }

    @EventHandler
    public void onChannelMessage(final ChannelMessageEvent e) {
        Channel channel = e.getChannel();
        Source author = e.getUser();
        String message = e.getMessage();

        if(Time.since(bot.getLastJoke()) > 30_000) {
            // Curlybear is op
            if(message.toLowerCase().contains("hue hue hue") && !bot.isBotAdmin(author.getName())) {
                channel.getServer().send(new ActionIrcPacket(channel.getName(), "instanciates his ban utility..."));
                bot.setLastJoke(Time.now());
            } else if(message.toLowerCase().startsWith("hue") && author.getName().toLowerCase().contains("bear")) {
                channel.sendMessage("hue? I guess you mean HUE HUE HUE!?");
                bot.setLastJoke(Time.now());
            }
        }
    }

    @EventHandler
    public void onBotKicked(ClientKickedFromChannelEvent e) {
        e.getChannel().getServer().removeChannel(e.getChannel().getName());
    }

    @EventHandler
    public void onUserPartChannel(UserPartChannelEvent e) {
        // Workaournd because Alix does not update user lists
        e.getChannel().updateUsers();
    }

    @EventHandler
    public void onConnectionLost(ClientLostConnectionEvent e) {
        // Workaround because Alix doesn't auto reconnect
        Georges.getThreadPool().submit(() -> {
            try {
                Thread.sleep(5_000);
            } catch(InterruptedException ignored) {}
            e.getServer().connect();
        });
        e.consume();
    }

    @EventHandler
    public void onConnectionFail(FailedToJoinServerEvent e) {
        // Workaround because Alix doesn't auto reconnect
        Georges.getThreadPool().submit(() -> {
            try {
                Thread.sleep(10_000);
            } catch(InterruptedException kitteh) {}
            e.getServer().connect();
        });
        e.consume();
    }
}
