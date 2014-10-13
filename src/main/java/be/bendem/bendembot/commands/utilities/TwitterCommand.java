package be.bendem.bendembot.commands.utilities;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.utils.StrUtils;
import be.bendem.bendembot.utils.TwitterApiUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.ribesg.alix.api.enums.Codes;

import java.util.List;

/**
 * @author bendem
 */
public class TwitterCommand extends BaseCommand {

    private final TwitterApiUtils twitterApiUtils;

    public TwitterCommand(TwitterApiUtils twitterApiUtils) {
        super("twitter", new String[] {
            "Twitter command - Usage ## <id|username>"
        }, "tw");
        this.twitterApiUtils = twitterApiUtils;
    }

    @Override
    public void exec(Context context, String primaryArgument, List<String> args) {
        if(args.size() == 0) {
            context.error("Not enough argument");
            return;
        }

        String arg = args.get(0);
        JsonObject tweet;

        if(arg.matches("[0-9]+")) {
            // Get tweet by id
            tweet = twitterApiUtils.getTweet(arg);
        } else {
            // Get last user tweet
            JsonArray timeline = twitterApiUtils.getUserTimeline(arg);
            tweet = timeline.get(0).getAsJsonObject();
        }

        if(tweet == null) {
            context.error("Error while loading tweet");
            return;
        }

        String message = tweet.get("text").getAsString() + " by @" + Codes.BOLD + tweet.getAsJsonObject("user").get("screen_name").getAsString();
        context.message(StrUtils.antiPing(message));
    }

}
