package be.bendem.bendembot.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.ribesg.alix.api.Log;
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
    private static final String API_KEY = "eQ0pSR4zVmvJaWSt9FetK3lok";
    private static final String API_KEY_SECRET = "D2nI8SNvW1LcQlG229jhULFkvbxd7zbVkquRu45j2jytFzr1hf";
    private static final String ACCESS_TOKEN = "414336935-wZXskNUxT1GGMG91uIlKr5lLLZIRBxcpeI6y8SIa";
    private static final String ACCESS_TOKEN_SECRET = "dOCsPkNOFG2Yo8iJJ3Jz8jurvHtW2j3C3b3g2jdE6Fe6x";

    private final OAuthService service;
    private final Token accessToken;

    public TwitterCommand() {
        super("twitter", new String[] {
            "Twitter command - Usage ##.<id|username>"
        }, "tw");

        service = new ServiceBuilder()
            .provider(TwitterApi.class)
            .apiKey(API_KEY)
            .apiSecret(API_KEY_SECRET)
            .build();
        accessToken = new Token(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
    }

    @Override
    public void exec(String primaryArgument, List<String> args) {
        Map<String, String> params = new HashMap<>();
        JsonObject tweet;

        if(primaryArgument.matches("[0-9]+")) {
            // Get tweet by id
            params.put(":id", primaryArgument);
            tweet = get("statuses/show/:id.json", params).getAsJsonObject();
        } else {
            // Get last user tweet
            params.put(":name", primaryArgument);
            JsonArray timeline = get("statuses/user_timeline.json?screen_name=:name", params).getAsJsonArray();
            tweet = timeline.get(0).getAsJsonObject();
        }

        if(tweet == null) {
            error("Error while loading tweet");
            return;
        }

        String message = tweet.get("text").getAsString() + " by @" + tweet.getAsJsonObject("user").get("screen_name").getAsString();
        message(message);
    }

    private JsonElement get(String route, Map<String, String> params) {
        final StringBuilder url = new StringBuilder(TWITTER_ENDPOINT).append(route);

        params.forEach((key, value)->url.replace(url.indexOf(key), url.indexOf(key) + key.length(), value));
        Log.info(url.toString());

        OAuthRequest request = new OAuthRequest(Verb.GET, url.toString());
        service.signRequest(accessToken, request);
        Response response = request.send();
        if(response.getBody() == null) {
            throw new RuntimeException("Api response body was null");
        }
        return new JsonParser().parse(response.getBody());
    }

}
