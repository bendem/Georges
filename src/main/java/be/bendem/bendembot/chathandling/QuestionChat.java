package be.bendem.bendembot.chathandling;

import be.bendem.bendembot.Context;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bendem
 */
public class QuestionChat implements ChatHandler {

    private final Pattern QUESTION = Pattern.compile("^Georges[^a-z].*\\?$");
    private final Random RANDOM = new Random();
    private final String[] ANSWERS = new String[] {
        "yes", "no", "maybe...", "totally!", "why not",
        "that will never happen"
    };

    @Override
    public void onChat(Context context, String message, Matcher matcher) {
        context.message(ANSWERS[RANDOM.nextInt(ANSWERS.length)]);
    }

    @Override
    public Matcher matcher(String message) {
        return QUESTION.matcher(message);
    }
}
