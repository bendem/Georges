package be.bendem.bendembot.chathandling;

import be.bendem.bendembot.Context;

import java.util.regex.Matcher;

/**
 * @author bendem
 */
public interface ChatHandler {

    public void onChat(Context context, String message, Matcher matcher);
    public Matcher matcher(String message);

}
