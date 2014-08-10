package be.bendem.bendembot.automatedmessages;

import be.bendem.bendembot.Context;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author bendem
 */
public class Message {

    private final String                         name;
    private final Set<MessageEventHandler.Event> events;
    private final List<String>                   messageData;
    private       String                         text;

    public Message(String name, String text) {
        this.name = name.toLowerCase();
        this.text = text;
        events = new HashSet<>();
        messageData = new ArrayList<>();
    }

    public String transform(Context context, Map<String, String> data) {
        String text = this.text;
        for(Map.Entry<String, String> entry : data.entrySet()) {
            text = StringUtils.replace(text, '{' + entry.getKey() + '}', entry.getValue());
        }
        for(MessageData messageData : MessageData.values()) {
            text = StringUtils.replace(text, '{' + messageData.name().toLowerCase() + '}', messageData.getData(context));
        }
        return text;
    }

    public String getName() {
        return name;
    }

    public List<String> getMessageData() {
        return messageData;
    }

    public Set<MessageEventHandler.Event> getEvents() {
        return events;
    }

    public void addEvent(MessageEventHandler.Event event) {
        events.add(event);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
