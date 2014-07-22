package be.bendem.bendembot.filters;

import be.bendem.bendembot.IrcClient;
import be.bendem.bendembot.utils.Time;
import be.bendem.bendembot.utils.Tuple;
import fr.ribesg.alix.api.Channel;
import fr.ribesg.alix.api.Log;
import fr.ribesg.alix.api.Source;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bendem
 */
public class MessageFilter implements ChatFilter {

    private static final int TIMES = 3;

    /**
     * structure:
     *
     *     channel name
     *         user host
     *             -> last message / count
     */
    private final Map<String, Map<String, Tuple<String, Integer>>> messageFilter = new HashMap<>();
    private final IrcClient bot;

    public MessageFilter(IrcClient bot) {
        this.bot = bot;
    }

    @Override
    public boolean handleMessage(Channel channel, Source author, String message) {
        // No use to filter spam if not op or if sender is admin
        //if(!channel.isOp(bot.getName()) || bot.isBotAdmin(author.getName())) {
        //    return false;
        //}

        Map<String, Tuple<String, Integer>> chanMap = messageFilter.get(channel.getName());
        if(chanMap == null) {
            chanMap = new HashMap<>();
            messageFilter.put(channel.getName(), chanMap);
            return false;
        }

        Tuple<String, Integer> messageTuple = chanMap.get(author.getHostName());
        if(messageTuple == null || !messageTuple.getA().equalsIgnoreCase(message)) {
            chanMap.put(author.getHostName(), new Tuple<>(message, 1));
            return false;
        }

        messageTuple.setB(messageTuple.getB()+1);
        if(messageTuple.getB() == TIMES) {
            if(Time.since(bot.getLastSpoke()) > 10_000) {
                channel.sendMessage(author.getName() + ", stop repeating yourself...");
                bot.setLastSpoke(Time.now());
            }
        } else if(messageTuple.getB() > TIMES) {
            Log.info("Kicking " + author.getName() + "...");
            bot.getUserManager().kick(channel, author, "Don't repeat yourself!");
            messageTuple.setB(0);
            return true;
        }
        return false;
    }

    @Override
    public void forgetUser(Source user, Channel channel) {
        Map<String, Tuple<String, Integer>> messageTuple = messageFilter.get(channel.getName());
        if(messageTuple != null) {
            messageTuple.remove(user.getHostName());
        }
    }

}
