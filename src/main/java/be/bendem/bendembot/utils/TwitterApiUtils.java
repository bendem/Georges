package be.bendem.bendembot.utils;

import be.bendem.bendembot.Georges;
import be.bendem.bendembot.configuration.Configuration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * @author bendem
 */
public class TwitterApiUtils {

    private static final String TWITTER_ENDPOINT = "https://api.twitter.com/1.1/";

    private final OAuthService service;
    private final Token token;

    public TwitterApiUtils(Configuration config) {
        this.service = new ServiceBuilder()
                .provider(org.scribe.builder.api.TwitterApi.class)
                .apiKey(config.getTwitterApiKey())
                .apiSecret(config.getTwitterApiKeySecret())
            .build();
        this.token = new Token(config.getTwitterAccessToken(), config.getTwitterAccessTokenSecret());
    }

    public JsonObject getTweet(String id) {
        return get("statuses/show/" + id + ".json").getAsJsonObject();
    }

    public JsonObject getUser(String user) {
        return get("users/show.json?screen_name=" + user).getAsJsonObject();
    }

    public JsonArray getUserTimeline(String user) {
        return get("statuses/user_timeline.json?screen_name=" + user).getAsJsonArray();
    }

    private JsonElement get(String uri) {
        final StringBuilder url = new StringBuilder(TWITTER_ENDPOINT).append(uri);

        Georges.getLogger().info(url.toString());

        OAuthRequest request = new OAuthRequest(Verb.GET, url.toString());
        service.signRequest(token, request);
        Response response = request.send();
        String answer = readAnswer(response);
        if(answer == null) {
            throw new RuntimeException("Api response body was null");
        }
        return new JsonParser().parse(answer);
    }

    private String readAnswer(Response response) {
        StringBuilder builder = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(response.getStream(), StandardCharsets.UTF_8))) {
            String input;
            while((input = reader.readLine()) != null) {
                builder.append(input);
            }
        } catch(IOException e) {
            Georges.getLogger().error("Error while reading ");
            return null;
        }
        return builder.toString();
    }

}
