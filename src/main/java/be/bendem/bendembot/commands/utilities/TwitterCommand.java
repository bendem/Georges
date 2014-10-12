package be.bendem.bendembot.commands.utilities;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.commands.BaseCommand;
import be.bendem.bendembot.utils.StrUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.ribesg.alix.api.Log;
import fr.ribesg.alix.api.enums.Codes;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bendem
 */
public class TwitterCommand extends BaseCommand {

    private static final String TWITTER_ENDPOINT = "https://api.twitter.com/1.1/";

    private final OAuthService service;
    private final Token        token;

    public TwitterCommand(String apiKey, String apiKeySecret, String accessToken, String accessTokenSecret) {
        super("twitter", new String[] {
            "Twitter command - Usage ## <id|username>"
        }, "tw");

        service = new ServiceBuilder().provider(TwitterApi.class).apiKey(apiKey).apiSecret(apiKeySecret).build();
        token = new Token(accessToken, accessTokenSecret);
    }

    @Override
    public void exec(Context context, String primaryArgument, List<String> args) {
        if(args.size() == 0) {
            context.error("Not enough argument");
            return;
        }

        String arg = args.get(0);
        Map<String, String> params = new HashMap<>();
        JsonObject tweet;

        if(arg.matches("[0-9]+")) {
            // Get tweet by id
            params.put(":id", arg);
            tweet = get("statuses/show/:id.json", params).getAsJsonObject();
        } else {
            // Get last user tweet
            params.put(":name", arg);
            JsonArray timeline = get("statuses/user_timeline.json?screen_name=:name", params).getAsJsonArray();
            tweet = timeline.get(0).getAsJsonObject();
        }

        if(tweet == null) {
            context.error("Error while loading tweet");
            return;
        }

        String message = tweet.get("text").getAsString() + " by @" + Codes.BOLD + tweet.getAsJsonObject("user").get("screen_name").getAsString();
        context.message(StrUtils.antiPing(message));
    }

    private JsonElement get(String route, Map<String, String> params) {
        final StringBuilder url = new StringBuilder(TWITTER_ENDPOINT).append(route);

        params.forEach((key, value)->url.replace(url.indexOf(key), url.indexOf(key) + key.length(), value));
        Log.info(url.toString());

        OAuthRequest request = new OAuthRequest(Verb.GET, url.toString());
        service.signRequest(token, request);
        Response response = request.send();
        if(response.getBody() == null) {
            throw new RuntimeException("Api response body was null");
        }
        return new JsonParser().parse(response.getBody());
    }

}
