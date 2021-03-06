package be.bendem.bendembot.utils;

import be.bendem.bendembot.Georges;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.ribesg.alix.api.bot.util.WebUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bendem
 */
public class GistStacks {

    private static final boolean ACTIVE = false;

    private static final Map<String, String> HEADERS;
    static {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/vnd.github.v3+json");
        headers.put("User-Agent", "GeorgesBot");
        HEADERS = Collections.unmodifiableMap(headers);
    }

    public static String gist(Throwable throwable) {
        if(!ACTIVE) {
            return "!!Gist system no active!!";
        }
        JsonObject json = new JsonObject();
        json.addProperty("description", "Georges stacktrace: " + throwable.getMessage());
        json.addProperty("public", false);
        JsonObject files = new JsonObject();
        JsonObject stackJson = new JsonObject();
        stackJson.addProperty("content", generateString(throwable));
        files.add("stacktrace.txt", stackJson);
        json.add("files", files);

        String data = new Gson().toJson(json);
        Georges.getLogger().debug(data);
        try {
            // TODO OAuth?
            String response = WebUtil.post("https://api.github.com/gists", "application/json", data, HEADERS);
            return new JsonParser().parse(response).getAsJsonObject().get("html_url").getAsString();
        } catch(IOException e) {
            Georges.getLogger().error("Could not post stacktrace", e);
            return "'nothing'. There was an error when posting the stacktrace: " + e.getMessage();
        }
    }

    private static String generateString(Throwable throwable) {
        StringBuilder builder = new StringBuilder();
        generateString(throwable, builder);
        return builder.toString();
    }

    private static void generateString(Throwable throwable, StringBuilder builder) {
        builder.append(throwable.getClass().getName());
        if(throwable.getMessage() != null) {
            builder.append(": ").append(throwable.getMessage()).append('\n');
        }
        for(StackTraceElement element : throwable.getStackTrace()) {
            builder
                    .append('\t')
                    .append(element.getClassName())
                    .append('(')
                    .append(element.getFileName() == null ? '~' : element.getFileName())
                    .append(':')
                    .append(element.getLineNumber() == -1 ? '~' : element.getLineNumber())
                    .append(")")
                    .append('\n');
        }
        if(throwable.getCause() != null) {
            generateString(throwable.getCause(), builder);
        }
    }

}
