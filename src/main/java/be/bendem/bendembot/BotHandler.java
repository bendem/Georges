package be.bendem.bendembot;

import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Server;
import fr.ribesg.alix.api.event.*;
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
