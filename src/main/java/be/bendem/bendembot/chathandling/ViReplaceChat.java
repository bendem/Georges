package be.bendem.bendembot.chathandling;

import be.bendem.bendembot.Context;
import fr.ribesg.alix.api.enums.Codes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bendem
 */
public class ViReplaceChat implements ChatHandler {

    private final Pattern VI_REPLACE_COMMAND = Pattern.compile("(?<type>s|g)/(?<regex>[^/]+)/(?<replace>[^/]+)/?");

    @Override
    public void onChat(Context context, String message, Matcher matcher) {
        // TODO Allow that to be disabled by channel
        // TODO Get previous user message
        String previous = "";
        String corrected;
        if(matcher.group("type").charAt(0) == 's') {
            corrected = previous.replaceFirst(matcher.group("regex"), matcher.group("replace"));
        } else {
            corrected = previous.replaceAll(matcher.group("regex"), matcher.group("replace"));
        }
        if(!message.equals(corrected)) {
            context.message(context.getUser().getName() + " meant " + Codes.BOLD + corrected + Codes.RESET);
        }
    }

    @Override
    public Matcher matcher(String message) {
        return VI_REPLACE_COMMAND.matcher(message);
    }
}
