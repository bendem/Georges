package be.bendem.bendembot.automatedmessages;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bendem
 */
public class MessageManager {

    private final Map<String, String>     data;
    private final Map<String, Message>     messages;

    public MessageManager() {
        this.data = new HashMap<>();
        this.messages = new HashMap<>();
    }

    public String getData(String key) {
        return data.get(key);
    }

    public void setData(String key, String value) {
        data.put(key, value);
    }

    public void removeData(String key) {
        data.remove(key);
    }

    public Message getMessage(String key) {
        return messages.get(key);
    }

    public void setMessage(Message message) {
        messages.put(message.getName(), message);
    }

    public void removeMessage(String key) {
        messages.remove(key);
    }

    public enum Event {
        UserJoinChannel,
        UserLeaveChannel,
        UserMessageChannel,
        UserGetBanned,
        UserGetKicked,
        UserGetVoiced,
        UserGetOpped,
        UserGetUnbanned,
        UserGetUnopped,
        UserGetUnvoiced
    }
}
