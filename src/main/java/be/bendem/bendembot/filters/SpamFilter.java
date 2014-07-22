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
public class SpamFilter implements ChatFilter {

    private static final int TIMES = 4;

    /**
     * Structure:
     *
     *     channel name
     *         user host
     *             last message / count
     */
    private final Map<String, Map<String, Tuple<Long, Integer>>> spamFilter = new HashMap<>();
    private final IrcClient bot;

    public SpamFilter(IrcClient bot) {
        this.bot = bot;
    }

    @Override
    public boolean handleMessage(Channel channel, Source author, String message) {
        // No use to filter spam if not op or if sender is admin
        //if(!channel.isOp(bot.getName()) || bot.isBotAdmin(author.getName())) {
        //    return false;
        //}

        long currentTime = Time.now();

        Map<String, Tuple<Long, Integer>> chanMap = spamFilter.get(channel.getName());
        if(chanMap == null) {
            chanMap = new HashMap<>();
            spamFilter.put(channel.getName(), chanMap);
            return false;
        }

        Tuple<Long, Integer> timeTuple = chanMap.get(author.getName());
        if(timeTuple == null) {
            timeTuple = new Tuple<>(currentTime, 1);
            chanMap.put(author.getName(), timeTuple);
            return false;
        }

        long previousTime = timeTuple.getA();
        timeTuple.setA(currentTime);

        if(Time.since(previousTime) < 2000) {
            timeTuple.setB(timeTuple.getB()+1);
            if(timeTuple.getB() == TIMES) {
                if(Time.since(bot.getLastSpoke()) > 10_000) {
                    channel.sendMessage(author.getName() + ", slow down...");
                    bot.setLastSpoke(currentTime);
                }
            } else if(timeTuple.getB() > TIMES) {
                Log.info("Kicking " + author.getName() + "...");
                bot.getUserManager().kick(channel, author, "Don't flood!");
                timeTuple.setA(currentTime);
                timeTuple.setB(0);
                return true;
            }
        }
        return false;
    }

    @Override
    public void forgetUser(Source user, Channel channel) {
        spamFilter.get(channel.getName()).remove(user.getHostName());
    }

}
