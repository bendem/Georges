package be.bendem.bendembot.automatedmessages;

import be.bendem.bendembot.Context;
import be.bendem.bendembot.usermanagement.UserManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bendem
 */
public class MessageManager {

    private final MessageEventHandler eventHandler;
    private final Map<String, String> data;
    private final Map<String, Message> messages;

    public MessageManager(UserManager userManager) {
        this.eventHandler = new MessageEventHandler(this, userManager);
        this.data = new HashMap<>();
        this.messages = new HashMap<>();
    }

    public String getData(String key) {
        return data.get(key.toLowerCase());
    }

    public void setData(String key, String value) {
        data.put(key.toLowerCase(), value);
    }

    public void removeData(String key) {
        data.remove(key.toLowerCase());
    }

    public Message getMessage(String key) {
        return messages.get(key.toLowerCase());
    }

    public String getTransformedMessage(String key, Context context) {
        Message message = getMessage(key.toLowerCase());
        if(message == null) {
            return null;
        }
        return message.transform(context, data);
    }

    public void setMessage(Message message) {
        messages.put(message.getName(), message);
    }

    public void removeMessage(String key) {
        messages.remove(key.toLowerCase());
    }

    public void spawnEvent(MessageEventHandler.Event event, Context context) {
        messages.values().stream()
                .filter(message -> message.shouldBeTriggered(event))
                .forEach(message -> context.message(message.transform(context, data), false));
    }

}
