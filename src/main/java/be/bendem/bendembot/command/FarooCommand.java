package be.bendem.bendembot.command;

import be.bendem.bendembot.utils.GistStacks;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.ribesg.alix.api.Log;
import fr.ribesg.alix.api.bot.util.WebUtil;
import fr.ribesg.alix.api.enums.Codes;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bendem
 */
public class FarooCommand extends BaseCommand {

    private static final Map<String, String> DEFAULT_HEADERS;
    static {
        HashMap<String, String> map = new HashMap<>();
        map.put("User-Agent", "GeorgesBot");
        DEFAULT_HEADERS = Collections.unmodifiableMap(map);
    }

    private static final String FAROO_KEY = "x7ZRD9aj8hFlz1vxVtA6M3-MqvA_";
    private static final String FAROO_ENDPOINT = "http://www.faroo.com/api?q=:query&start=1&length=1&l=en&src=:src&kwic=true&i=false&f=json&key=" + FAROO_KEY;

    public FarooCommand() {
        super("faroo", new String[]{
            "Search for something using the Faroo api - Usage: ##.<web|news|topics|trends|suggest> research"
        });
    }

    @Override
    protected void exec(String primaryArgument, List<String> args) {
        Source source = Source.get(primaryArgument);
        if(source == null) {
            source = Source.web;
        }
        if(source == Source.topics) {
            error("This type is not supported yet (and will never be AHAHAHAHAHAHAHAH!)");
            return;
        }
        String response = get(StringUtils.join(args, ' '), source);
        if(response == null) {
            error("Error while fetching datas");
            return;
        }

        String message = null;
        switch(source) {
            case news:
            case web:
                message = getContent(response);
                break;
            /*case topics:
                message = getTopics(response);
                break;*/
            case trends:
                message = getTrends(response);
                break;
            case suggest:
                message = getSuggestion(response);
                break;
        }
        if(message != null) {
            message(message);
        }
    }

    private String getTrends(String json) {
        JsonObject jsonParsed = new JsonParser().parse(json).getAsJsonObject();
        JsonArray trends = jsonParsed.getAsJsonArray("trends");
        int count = jsonParsed.get("count").getAsInt();

        return "Trends (" + count + "): " + StringUtils.join(trends, ", ") + '.';
    }

    private String getSuggestion(String json) {
        JsonArray suggestionJson = new JsonParser().parse(json).getAsJsonArray();
        JsonArray suggestions = suggestionJson.get(1).getAsJsonArray();
        if(suggestions.size() == 0) {
            return Codes.RED + "No suggestions.";
        }
        final StringBuilder builder = new StringBuilder("Suggestions for ")
                .append(Codes.BOLD)
                .append(suggestionJson.get(0).getAsString())
                .append(Codes.BOLD)
                .append(": ");
        suggestions.forEach((elem)->builder.append(elem.getAsString()).append(", "));
        return builder.delete(builder.length()-2, builder.length()).append('.').toString();
    }

    private String getContent(String json) {
        JsonObject search = new JsonParser().parse(json).getAsJsonObject();

        JsonArray results = search.getAsJsonArray("results");
        if(results == null || results.size() == 0) {
            error("No result");
            return null;
        }

        JsonObject result = results.get(0).getAsJsonObject();
        String title = result.get("title").getAsString();
        String url = result.get("url").getAsString();
        String content = stripHtml(result.get("kwic").getAsString());
        return Codes.BOLD + title + Codes.BOLD + ": " + content + " (" + url + ')';
    }

    private String get(String query, Source src) {
        try {
            return WebUtil.get(FAROO_ENDPOINT.replace(":query", URLEncoder.encode(query, "utf-8")).replace(":src", src.name().toLowerCase()), DEFAULT_HEADERS);
        } catch(IOException e) {
            error("Such stacktrace: " + GistStacks.gist(e));
            e.printStackTrace();
            return null;
        }
    }

    private String stripHtml(String html) {
        return Jsoup.parse(html).text();
    }

    private enum Source {
        web, news, topics, trends, suggest;

        public static Source get(String primaryArgument) {
            for(Source source : values()) {
                if(source.name().equalsIgnoreCase(primaryArgument)) {
                    return source;
                }
            }
            return null;
        }
    }

}
