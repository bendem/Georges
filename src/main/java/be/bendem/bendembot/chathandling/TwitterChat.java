package be.bendem.bendembot.chathandling;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.utils.StrUtils;
import be.bendem.bendembot.utils.TwitterApiUtils;
import com.google.gson.JsonObject;
import fr.ribesg.alix.api.enums.Codes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bendem
 */
public class TwitterChat implements ChatHandler {

    private static final Pattern TWITTER_URL = Pattern.compile("twitter.com/(?<user>[-_a-zA-Z0-9]+)(?:/status/(?<id>[0-9]+))?");
    private final TwitterApiUtils twitterApiUtils;

    public TwitterChat(TwitterApiUtils twitterApiUtils) {
        this.twitterApiUtils = twitterApiUtils;
    }

    @Override
    public void onChat(Context context, String message, Matcher matcher) {
        String id = matcher.group("id");
        String screenName = matcher.group("user");

        String toSend;
        if(id == null || id.isEmpty()) {
            JsonObject user = twitterApiUtils.getUser(screenName);
            toSend = user.get("name").getAsString()
                    + " (@" + user.get("screen_name").getAsString() + ") "
                    + user.get("followers_count").getAsInt() + " followers, "
                    + user.get("friends_count").getAsInt() + " followings, "
                    + user.get("statuses_count") + " tweets "
                    // Is twitter account protected?
                    + "(" + (user.get("protected").getAsBoolean() ? Codes.RED + "\u2718" : Codes.LIGHT_GREEN + "\u2713") + Codes.RESET + ").";
        } else {
            //tweet
            JsonObject tweet = twitterApiUtils.getTweet(id);
            toSend = tweet.get("text").getAsString()
                    + " by @" + Codes.BOLD + tweet.getAsJsonObject("user").get("screen_name").getAsString();
        }
        context.message(StrUtils.antiPing(toSend), false);
    }

    @Override
    public Matcher matcher(String message) {
        return TWITTER_URL.matcher(message);
    }

}
